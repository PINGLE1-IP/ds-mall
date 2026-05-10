package com.ds.mall.order.controller;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.Result;
import com.ds.mall.order.dto.CreateOrderRequest;
import com.ds.mall.order.dto.OrderPageQuery;
import com.ds.mall.order.service.OrderService;
import com.ds.mall.order.vo.OrderCreateVO;
import com.ds.mall.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "订单接口")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<OrderCreateVO> create(@Valid @RequestBody CreateOrderRequest request) {
        return Result.success(orderService.createOrder(request));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderVO> detail(@PathVariable String orderNo) {
        return Result.success(orderService.detail(orderNo));
    }

    @Operation(summary = "查询当前用户订单列表")
    @GetMapping("/my")
    public Result<PageResult<OrderVO>> myOrders(@ParameterObject OrderPageQuery query) {
        return Result.success(orderService.myOrders(query));
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancel(@PathVariable String orderNo) {
        orderService.cancel(orderNo);
        return Result.success();
    }

    @Operation(summary = "模拟支付订单")
    @PutMapping("/{orderNo}/pay")
    public Result<Void> pay(@PathVariable String orderNo) {
        orderService.pay(orderNo);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @PutMapping("/{orderNo}/confirm")
    public Result<Void> confirm(@PathVariable String orderNo) {
        orderService.confirm(orderNo);
        return Result.success();
    }
}
