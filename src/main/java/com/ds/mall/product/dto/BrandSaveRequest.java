package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "品牌保存请求")
public class BrandSaveRequest {

    @NotBlank(message = "品牌名称不能为空")
    private String name;
    private String logoUrl;
    private String description;
    private Integer enabled = 1;
}
