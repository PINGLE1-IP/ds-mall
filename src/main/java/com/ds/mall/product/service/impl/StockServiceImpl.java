package com.ds.mall.product.service.impl;

import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.product.entity.ProductSku;
import com.ds.mall.product.mapper.ProductSkuMapper;
import com.ds.mall.product.service.StockService;
import com.ds.mall.product.vo.StockVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final ProductSkuMapper skuMapper;

    @Override
    public StockVO getStock(Long skuId) {
        ProductSku sku = requireSku(skuId);
        return toStockVO(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increase(Long skuId, Integer quantity) {
        requireSku(skuId);
        skuMapper.increaseStock(skuId, quantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long skuId, Integer quantity) {
        requireSku(skuId);
        if (skuMapper.deductAvailableStock(skuId, quantity) == 0) {
            throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(Long skuId, Integer quantity) {
        requireSku(skuId);
        if (skuMapper.lockStock(skuId, quantity) == 0) {
            throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void release(Long skuId, Integer quantity) {
        requireSku(skuId);
        if (skuMapper.releaseLockedStock(skuId, quantity) == 0) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "锁定库存不足");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductLocked(Long skuId, Integer quantity) {
        requireSku(skuId);
        if (skuMapper.deductLockedStock(skuId, quantity) == 0) {
            throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
        }
    }

    private ProductSku requireSku(Long skuId) {
        ProductSku sku = skuMapper.selectById(skuId);
        if (sku == null) {
            throw new BusinessException(ResultCode.SKU_NOT_FOUND);
        }
        return sku;
    }

    private StockVO toStockVO(ProductSku sku) {
        StockVO vo = new StockVO();
        vo.setSkuId(sku.getId());
        vo.setStock(sku.getStock());
        vo.setLockedStock(sku.getLockedStock());
        vo.setAvailableStock(sku.getStock() - sku.getLockedStock());
        return vo;
    }
}
