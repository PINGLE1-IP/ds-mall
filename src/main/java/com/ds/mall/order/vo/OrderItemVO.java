package com.ds.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {

    private Long id;
    private Long skuId;
    private String skuName;
    private String skuImage;
    private BigDecimal salePrice;
    private Integer quantity;
    private BigDecimal totalAmount;
}
