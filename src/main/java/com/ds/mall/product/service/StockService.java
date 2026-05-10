package com.ds.mall.product.service;

import com.ds.mall.product.vo.StockVO;

public interface StockService {

    StockVO getStock(Long skuId);

    void increase(Long skuId, Integer quantity);

    void deduct(Long skuId, Integer quantity);

    void lock(Long skuId, Integer quantity);

    void release(Long skuId, Integer quantity);

    void deductLocked(Long skuId, Integer quantity);
}
