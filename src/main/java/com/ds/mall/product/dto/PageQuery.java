package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用分页查询")
public class PageQuery {

    private Long current = 1L;
    private Long size = 10L;
    private String keyword;
}
