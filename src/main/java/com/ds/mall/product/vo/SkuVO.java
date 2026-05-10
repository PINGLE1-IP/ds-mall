package com.ds.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuVO {

    private Long id;
    private Long spuId;
    private String skuCode;
    private String name;
    private BigDecimal salePrice;
    private BigDecimal marketPrice;
    private Integer stock;
    private Integer lockedStock;
    private Integer availableStock;
    private String specJson;
    private Integer enabled;
}
