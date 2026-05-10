package com.ds.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.util.BeanCopyUtils;
import com.ds.mall.product.dto.CategorySaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.entity.ProductCategory;
import com.ds.mall.product.mapper.ProductCategoryMapper;
import com.ds.mall.product.service.CategoryService;
import com.ds.mall.product.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_TREE_KEY = "mall:category:tree";

    private final ProductCategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CategorySaveRequest request) {
        ProductCategory category = new ProductCategory();
        fillCategory(category, request);
        categoryMapper.insert(category);
        category.setPath(buildPath(category));
        categoryMapper.updateById(category);
        redisTemplate.delete(CATEGORY_TREE_KEY);
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, CategorySaveRequest request) {
        ProductCategory category = requireCategory(id);
        fillCategory(category, request);
        category.setPath(buildPath(category));
        categoryMapper.updateById(category);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireCategory(id);
        categoryMapper.deleteById(id);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Integer enabled) {
        ProductCategory category = requireCategory(id);
        category.setEnabled(enabled);
        categoryMapper.updateById(category);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }

    @Override
    public CategoryVO detail(Long id) {
        return toVO(requireCategory(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryVO> tree(boolean enabledOnly) {
        if (enabledOnly) {
            Object cached = redisTemplate.opsForValue().get(CATEGORY_TREE_KEY);
            if (cached instanceof List<?> list) {
                return (List<CategoryVO>) list;
            }
        }
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<ProductCategory>()
                .eq(enabledOnly, ProductCategory::getEnabled, 1)
                .orderByAsc(ProductCategory::getLevel, ProductCategory::getSortOrder, ProductCategory::getId);
        List<CategoryVO> tree = buildTree(BeanCopyUtils.copyList(categoryMapper.selectList(wrapper), CategoryVO.class));
        if (enabledOnly) {
            redisTemplate.opsForValue().set(CATEGORY_TREE_KEY, tree);
        }
        return tree;
    }

    @Override
    public PageResult<CategoryVO> page(PageQuery query) {
        Page<ProductCategory> page = categoryMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<ProductCategory>()
                        .like(StringUtils.hasText(query.getKeyword()), ProductCategory::getName, query.getKeyword())
                        .orderByAsc(ProductCategory::getLevel, ProductCategory::getSortOrder));
        Page<CategoryVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(BeanCopyUtils.copyList(page.getRecords(), CategoryVO.class));
        return PageResult.of(voPage);
    }

    private void fillCategory(ProductCategory category, CategorySaveRequest request) {
        Long parentId = request.getParentId() == null ? 0L : request.getParentId();
        category.setParentId(parentId);
        category.setName(request.getName());
        category.setSortOrder(request.getSortOrder());
        category.setEnabled(request.getEnabled());
        category.setIconUrl(request.getIconUrl());
        if (parentId == 0) {
            category.setLevel(1);
        } else {
            ProductCategory parent = requireCategory(parentId);
            category.setLevel(parent.getLevel() + 1);
        }
    }

    private ProductCategory requireCategory(Long id) {
        ProductCategory category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }
        return category;
    }

    private String buildPath(ProductCategory category) {
        if (category.getParentId() == null || category.getParentId() == 0) {
            return "/" + category.getId();
        }
        ProductCategory parent = requireCategory(category.getParentId());
        return parent.getPath() + "/" + category.getId();
    }

    private List<CategoryVO> buildTree(List<CategoryVO> categories) {
        Map<Long, List<CategoryVO>> group = categories.stream().collect(Collectors.groupingBy(CategoryVO::getParentId));
        categories.forEach(category -> category.setChildren(group.getOrDefault(category.getId(), List.of()).stream()
                .sorted(Comparator.comparing(CategoryVO::getSortOrder).thenComparing(CategoryVO::getId))
                .toList()));
        return group.getOrDefault(0L, List.of()).stream()
                .sorted(Comparator.comparing(CategoryVO::getSortOrder).thenComparing(CategoryVO::getId))
                .toList();
    }

    private CategoryVO toVO(ProductCategory category) {
        return BeanCopyUtils.copy(category, CategoryVO.class);
    }
}
