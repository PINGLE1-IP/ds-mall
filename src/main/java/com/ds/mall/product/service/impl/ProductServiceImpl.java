package com.ds.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.util.BeanCopyUtils;
import com.ds.mall.product.dto.ProductPageQuery;
import com.ds.mall.product.dto.SkuSaveRequest;
import com.ds.mall.product.dto.SpuSaveRequest;
import com.ds.mall.product.entity.ProductBrand;
import com.ds.mall.product.entity.ProductCategory;
import com.ds.mall.product.entity.ProductSku;
import com.ds.mall.product.entity.ProductSpu;
import com.ds.mall.product.mapper.ProductBrandMapper;
import com.ds.mall.product.mapper.ProductCategoryMapper;
import com.ds.mall.product.mapper.ProductSkuMapper;
import com.ds.mall.product.mapper.ProductSpuMapper;
import com.ds.mall.product.service.ProductService;
import com.ds.mall.product.vo.SkuVO;
import com.ds.mall.product.vo.SpuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String SPU_CACHE_PREFIX = "mall:product:spu:";

    private final ProductSpuMapper spuMapper;
    private final ProductSkuMapper skuMapper;
    private final ProductCategoryMapper categoryMapper;
    private final ProductBrandMapper brandMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSpu(SpuSaveRequest request) {
        validateCategoryAndBrand(request.getCategoryId(), request.getBrandId());
        ProductSpu spu = new ProductSpu();
        fillSpu(spu, request);
        spu.setSaleStatus("OFF_SALE");
        spu.setAuditStatus("PENDING");
        spuMapper.insert(spu);
        return spu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(Long id, SpuSaveRequest request) {
        validateCategoryAndBrand(request.getCategoryId(), request.getBrandId());
        ProductSpu spu = requireSpu(id);
        fillSpu(spu, request);
        spuMapper.updateById(spu);
        evictSpu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpu(Long id) {
        requireSpu(id);
        spuMapper.deleteById(id);
        skuMapper.delete(new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getSpuId, id));
        evictSpu(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SpuVO detail(Long id) {
        String key = SPU_CACHE_PREFIX + id;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof SpuVO spuVO) {
            return spuVO;
        }
        ProductSpu spu = requireSpu(id);
        SpuVO vo = toSpuVO(spu);
        vo.setSkus(listSkusBySpuId(id));
        redisTemplate.opsForValue().set(key, vo, 30, TimeUnit.MINUTES);
        return vo;
    }

    @Override
    public PageResult<SpuVO> page(ProductPageQuery query) {
        LambdaQueryWrapper<ProductSpu> wrapper = new LambdaQueryWrapper<ProductSpu>()
                .and(StringUtils.hasText(query.getKeyword()), w -> w
                        .like(ProductSpu::getName, query.getKeyword())
                        .or()
                        .like(ProductSpu::getSpuCode, query.getKeyword()))
                .eq(query.getCategoryId() != null, ProductSpu::getCategoryId, query.getCategoryId())
                .eq(query.getBrandId() != null, ProductSpu::getBrandId, query.getBrandId())
                .eq(StringUtils.hasText(query.getSaleStatus()), ProductSpu::getSaleStatus, query.getSaleStatus())
                .eq(StringUtils.hasText(query.getAuditStatus()), ProductSpu::getAuditStatus, query.getAuditStatus())
                .ge(query.getMinPrice() != null, ProductSpu::getMinPrice, query.getMinPrice())
                .le(query.getMaxPrice() != null, ProductSpu::getMinPrice, query.getMaxPrice())
                .ge(query.getCreatedStartTime() != null, ProductSpu::getCreatedAt, query.getCreatedStartTime())
                .le(query.getCreatedEndTime() != null, ProductSpu::getCreatedAt, query.getCreatedEndTime())
                .orderByDesc(ProductSpu::getCreatedAt);
        Page<ProductSpu> page = spuMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        Page<SpuVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::toSpuVO).toList());
        return PageResult.of(voPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onSale(Long id) {
        ProductSpu spu = requireSpu(id);
        if (!"APPROVED".equals(spu.getAuditStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "商品审核通过后才能上架");
        }
        spu.setSaleStatus("ON_SALE");
        spuMapper.updateById(spu);
        evictSpu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offSale(Long id) {
        ProductSpu spu = requireSpu(id);
        spu.setSaleStatus("OFF_SALE");
        spuMapper.updateById(spu);
        evictSpu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPass(Long id) {
        ProductSpu spu = requireSpu(id);
        spu.setAuditStatus("APPROVED");
        spuMapper.updateById(spu);
        evictSpu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditReject(Long id) {
        ProductSpu spu = requireSpu(id);
        spu.setAuditStatus("REJECTED");
        spu.setSaleStatus("OFF_SALE");
        spuMapper.updateById(spu);
        evictSpu(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSku(SkuSaveRequest request) {
        requireSpu(request.getSpuId());
        ProductSku sku = new ProductSku();
        fillSku(sku, request);
        sku.setLockedStock(0);
        sku.setVersion(0);
        skuMapper.insert(sku);
        evictSpu(request.getSpuId());
        return sku.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSku(Long id, SkuSaveRequest request) {
        requireSpu(request.getSpuId());
        ProductSku sku = requireSku(id);
        fillSku(sku, request);
        skuMapper.updateById(sku);
        evictSpu(request.getSpuId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSku(Long id) {
        ProductSku sku = requireSku(id);
        skuMapper.deleteById(id);
        evictSpu(sku.getSpuId());
    }

    @Override
    public List<SkuVO> listSkusBySpuId(Long spuId) {
        requireSpu(spuId);
        return skuMapper.selectList(new LambdaQueryWrapper<ProductSku>()
                        .eq(ProductSku::getSpuId, spuId)
                        .orderByAsc(ProductSku::getId))
                .stream()
                .map(this::toSkuVO)
                .toList();
    }

    private void validateCategoryAndBrand(Long categoryId, Long brandId) {
        ProductCategory category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }
        ProductBrand brand = brandMapper.selectById(brandId);
        if (brand == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "品牌不存在");
        }
    }

    private ProductSpu requireSpu(Long id) {
        ProductSpu spu = spuMapper.selectById(id);
        if (spu == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        return spu;
    }

    private ProductSku requireSku(Long id) {
        ProductSku sku = skuMapper.selectById(id);
        if (sku == null) {
            throw new BusinessException(ResultCode.SKU_NOT_FOUND);
        }
        return sku;
    }

    private void fillSpu(ProductSpu spu, SpuSaveRequest request) {
        spu.setSpuCode(request.getSpuCode());
        spu.setName(request.getName());
        spu.setSubtitle(request.getSubtitle());
        spu.setCategoryId(request.getCategoryId());
        spu.setBrandId(request.getBrandId());
        spu.setMainImage(request.getMainImage());
        spu.setMinPrice(request.getMinPrice());
        spu.setDescription(request.getDescription());
    }

    private void fillSku(ProductSku sku, SkuSaveRequest request) {
        sku.setSpuId(request.getSpuId());
        sku.setSkuCode(request.getSkuCode());
        sku.setName(request.getName());
        sku.setSalePrice(request.getSalePrice());
        sku.setMarketPrice(request.getMarketPrice());
        sku.setStock(request.getStock());
        sku.setSpecJson(request.getSpecJson());
        sku.setEnabled(request.getEnabled());
    }

    private SpuVO toSpuVO(ProductSpu spu) {
        return BeanCopyUtils.copy(spu, SpuVO.class);
    }

    private SkuVO toSkuVO(ProductSku sku) {
        SkuVO vo = BeanCopyUtils.copy(sku, SkuVO.class);
        vo.setAvailableStock(sku.getStock() - sku.getLockedStock());
        return vo;
    }

    private void evictSpu(Long id) {
        redisTemplate.delete(SPU_CACHE_PREFIX + id);
    }
}
