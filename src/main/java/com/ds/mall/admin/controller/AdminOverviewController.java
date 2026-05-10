package com.ds.mall.admin.controller;

import com.ds.mall.admin.dto.AdminOverviewQuery;
import com.ds.mall.admin.service.AdminOverviewService;
import com.ds.mall.admin.vo.AdminOverviewVO;
import com.ds.mall.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台概览")
@RestController
@RequestMapping("/api/admin/overview")
@RequiredArgsConstructor
public class AdminOverviewController {

    private final AdminOverviewService adminOverviewService;

    @Operation(summary = "查询后台概览数据")
    @GetMapping
    public Result<AdminOverviewVO> overview(@ParameterObject AdminOverviewQuery query) {
        return Result.success(adminOverviewService.overview(query));
    }
}
