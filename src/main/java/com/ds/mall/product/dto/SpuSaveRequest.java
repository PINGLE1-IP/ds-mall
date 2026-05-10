package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "SPU 保存请求")
public class SpuSaveRequest {

    @NotBlank(message = "SPU 编码不能为空")
    private String spuCode;
    @NotBlank(message = "商品名称不能为空")
    private String name;
    private String subtitle;
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    @NotNull(message = "品牌不能为空")
    private Long brandId;
    private String mainImage;
    @NotNull(message = "最低价不能为空")
    @DecimalMin(value = "0.00", message = "价格不能小于 0")
    private BigDecimal minPrice;
    private String description;
}
