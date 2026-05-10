package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "后台商品分页查询")
public class ProductPageQuery {

    private Long current = 1L;
    private Long size = 10L;
    private String keyword;
    private Long categoryId;
    private Long brandId;
    private String saleStatus;
    private String auditStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdEndTime;
}
