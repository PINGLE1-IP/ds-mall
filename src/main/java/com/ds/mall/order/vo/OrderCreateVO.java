package com.ds.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateVO {

    private String orderNo;
    private BigDecimal payAmount;
}
