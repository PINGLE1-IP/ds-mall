package com.ds.mall.product.controller;

import com.ds.mall.common.result.Result;
import com.ds.mall.product.dto.StockUpdateRequest;
import com.ds.mall.product.service.StockService;
import com.ds.mall.product.vo.StockVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台库存管理")
@RestController
@RequestMapping("/api/admin/skus")
@RequiredArgsConstructor
public class AdminStockController {

    private final StockService stockService;

    @Operation(summary = "查询 SKU 库存")
    @GetMapping("/{skuId}/stock")
    public Result<StockVO> getStock(@PathVariable Long skuId) {
        return Result.success(stockService.getStock(skuId));
    }

    @Operation(summary = "增加库存")
    @PutMapping("/{skuId}/stock/increase")
    public Result<Void> increase(@PathVariable Long skuId, @Valid @RequestBody StockUpdateRequest request) {
        stockService.increase(skuId, request.getQuantity());
        return Result.success();
    }

    @Operation(summary = "扣减可售库存")
    @PutMapping("/{skuId}/stock/deduct")
    public Result<Void> deduct(@PathVariable Long skuId, @Valid @RequestBody StockUpdateRequest request) {
        stockService.deduct(skuId, request.getQuantity());
        return Result.success();
    }

    @Operation(summary = "锁定库存")
    @PutMapping("/{skuId}/stock/lock")
    public Result<Void> lock(@PathVariable Long skuId, @Valid @RequestBody StockUpdateRequest request) {
        stockService.lock(skuId, request.getQuantity());
        return Result.success();
    }

    @Operation(summary = "释放锁定库存")
    @PutMapping("/{skuId}/stock/release")
    public Result<Void> release(@PathVariable Long skuId, @Valid @RequestBody StockUpdateRequest request) {
        stockService.release(skuId, request.getQuantity());
        return Result.success();
    }

    @Operation(summary = "扣减锁定库存")
    @PutMapping("/{skuId}/stock/deduct-locked")
    public Result<Void> deductLocked(@PathVariable Long skuId, @Valid @RequestBody StockUpdateRequest request) {
        stockService.deductLocked(skuId, request.getQuantity());
        return Result.success();
    }
}
