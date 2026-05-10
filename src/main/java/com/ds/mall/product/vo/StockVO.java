package com.ds.mall.product.vo;

import lombok.Data;

@Data
public class StockVO {

    private Long skuId;
    private Integer stock;
    private Integer lockedStock;
    private Integer availableStock;
}
