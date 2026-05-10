package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "SKU 保存请求")
public class SkuSaveRequest {

    @NotNull(message = "SPU ID 不能为空")
    private Long spuId;
    @NotBlank(message = "SKU 编码不能为空")
    private String skuCode;
    @NotBlank(message = "SKU 名称不能为空")
    private String name;
    @NotNull(message = "销售价不能为空")
    @DecimalMin(value = "0.00", message = "销售价不能小于 0")
    private BigDecimal salePrice;
    @NotNull(message = "市场价不能为空")
    @DecimalMin(value = "0.00", message = "市场价不能小于 0")
    private BigDecimal marketPrice;
    @Min(value = 0, message = "库存不能小于 0")
    private Integer stock = 0;
    private String specJson;
    private Integer enabled = 1;
}
