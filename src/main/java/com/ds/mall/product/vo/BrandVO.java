package com.ds.mall.product.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandVO {

    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private Integer enabled;
    private LocalDateTime createdAt;
}
