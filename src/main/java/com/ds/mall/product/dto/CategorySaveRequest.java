package com.ds.mall.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "分类保存请求")
public class CategorySaveRequest {

    @Schema(description = "父级分类 ID，根节点传 0")
    private Long parentId = 0L;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Min(value = 0, message = "排序值不能小于 0")
    private Integer sortOrder = 0;

    @Schema(description = "是否启用：1 启用，0 禁用")
    private Integer enabled = 1;

    private String iconUrl;
}
