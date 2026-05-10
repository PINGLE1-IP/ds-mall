package com.ds.mall.admin.vo;

import lombok.Data;

@Data
public class AdminOverviewVO {

    private Long userCount;
    private Long spuCount;
    private Long skuCount;
    private Long orderCount;
    private Long pendingOrderCount;
}
