package com.ds.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.util.BeanCopyUtils;
import com.ds.mall.product.dto.BrandSaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.entity.ProductBrand;
import com.ds.mall.product.mapper.ProductBrandMapper;
import com.ds.mall.product.service.BrandService;
import com.ds.mall.product.vo.BrandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final ProductBrandMapper brandMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BrandSaveRequest request) {
        ProductBrand brand = new ProductBrand();
        fillBrand(brand, request);
        brandMapper.insert(brand);
        return brand.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, BrandSaveRequest request) {
        ProductBrand brand = requireBrand(id);
        fillBrand(brand, request);
        brandMapper.updateById(brand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireBrand(id);
        brandMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Integer enabled) {
        ProductBrand brand = requireBrand(id);
        brand.setEnabled(enabled);
        brandMapper.updateById(brand);
    }

    @Override
    public BrandVO detail(Long id) {
        return BeanCopyUtils.copy(requireBrand(id), BrandVO.class);
    }

    @Override
    public PageResult<BrandVO> page(PageQuery query) {
        Page<ProductBrand> page = brandMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<ProductBrand>()
                        .like(StringUtils.hasText(query.getKeyword()), ProductBrand::getName, query.getKeyword())
                        .orderByDesc(ProductBrand::getCreatedAt));
        Page<BrandVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(BeanCopyUtils.copyList(page.getRecords(), BrandVO.class));
        return PageResult.of(voPage);
    }

    private ProductBrand requireBrand(Long id) {
        ProductBrand brand = brandMapper.selectById(id);
        if (brand == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "品牌不存在");
        }
        return brand;
    }

    private void fillBrand(ProductBrand brand, BrandSaveRequest request) {
        brand.setName(request.getName());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setDescription(request.getDescription());
        brand.setEnabled(request.getEnabled());
    }
}
