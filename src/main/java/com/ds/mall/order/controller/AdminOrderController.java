package com.ds.mall.order.controller;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.Result;
import com.ds.mall.order.dto.OrderPageQuery;
import com.ds.mall.order.service.OrderService;
import com.ds.mall.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台订单管理")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "后台分页查询订单")
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> page(@ParameterObject OrderPageQuery query) {
        return Result.success(orderService.adminPage(query));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderVO> detail(@PathVariable String orderNo) {
        return Result.success(orderService.detail(orderNo));
    }

    @Operation(summary = "订单发货")
    @PutMapping("/{orderNo}/ship")
    public Result<Void> ship(@PathVariable String orderNo) {
        orderService.ship(orderNo);
        return Result.success();
    }
}
