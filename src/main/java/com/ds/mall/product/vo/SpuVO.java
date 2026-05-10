package com.ds.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SpuVO {

    private Long id;
    private String spuCode;
    private String name;
    private String subtitle;
    private Long categoryId;
    private Long brandId;
    private String mainImage;
    private BigDecimal minPrice;
    private String saleStatus;
    private String auditStatus;
    private String description;
    private LocalDateTime createdAt;
    private List<SkuVO> skus;
}
