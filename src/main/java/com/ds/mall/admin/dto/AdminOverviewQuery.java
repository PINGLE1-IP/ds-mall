package com.ds.mall.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "后台概览查询")
public class AdminOverviewQuery {

    @Schema(description = "保留字段，便于后续扩展")
    private String scope;
}
