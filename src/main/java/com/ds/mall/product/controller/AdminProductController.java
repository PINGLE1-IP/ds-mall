package com.ds.mall.product.controller;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.Result;
import com.ds.mall.product.dto.ProductPageQuery;
import com.ds.mall.product.dto.SkuSaveRequest;
import com.ds.mall.product.dto.SpuSaveRequest;
import com.ds.mall.product.service.ProductService;
import com.ds.mall.product.vo.SkuVO;
import com.ds.mall.product.vo.SpuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "后台商品管理")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @Operation(summary = "新增 SPU")
    @PostMapping("/spu")
    public Result<Long> createSpu(@Valid @RequestBody SpuSaveRequest request) {
        return Result.success(productService.createSpu(request));
    }

    @Operation(summary = "修改 SPU")
    @PutMapping("/spu/{id}")
    public Result<Void> updateSpu(@PathVariable Long id, @Valid @RequestBody SpuSaveRequest request) {
        productService.updateSpu(id, request);
        return Result.success();
    }

    @Operation(summary = "删除 SPU")
    @DeleteMapping("/spu/{id}")
    public Result<Void> deleteSpu(@PathVariable Long id) {
        productService.deleteSpu(id);
        return Result.success();
    }

    @Operation(summary = "查询 SPU 详情")
    @GetMapping("/spu/{id}")
    public Result<SpuVO> detail(@PathVariable Long id) {
        return Result.success(productService.detail(id));
    }

    @Operation(summary = "后台 SPU 分页查询")
    @GetMapping("/spu/page")
    public Result<PageResult<SpuVO>> page(@ParameterObject ProductPageQuery query) {
        return Result.success(productService.page(query));
    }

    @Operation(summary = "商品上架")
    @PutMapping("/spu/{id}/on-sale")
    public Result<Void> onSale(@PathVariable Long id) {
        productService.onSale(id);
        return Result.success();
    }

    @Operation(summary = "商品下架")
    @PutMapping("/spu/{id}/off-sale")
    public Result<Void> offSale(@PathVariable Long id) {
        productService.offSale(id);
        return Result.success();
    }

    @Operation(summary = "审核通过")
    @PutMapping("/spu/{id}/audit-pass")
    public Result<Void> auditPass(@PathVariable Long id) {
        productService.auditPass(id);
        return Result.success();
    }

    @Operation(summary = "审核拒绝")
    @PutMapping("/spu/{id}/audit-reject")
    public Result<Void> auditReject(@PathVariable Long id) {
        productService.auditReject(id);
        return Result.success();
    }

    @Operation(summary = "新增 SKU")
    @PostMapping("/sku")
    public Result<Long> createSku(@Valid @RequestBody SkuSaveRequest request) {
        return Result.success(productService.createSku(request));
    }

    @Operation(summary = "修改 SKU")
    @PutMapping("/sku/{id}")
    public Result<Void> updateSku(@PathVariable Long id, @Valid @RequestBody SkuSaveRequest request) {
        productService.updateSku(id, request);
        return Result.success();
    }

    @Operation(summary = "删除 SKU")
    @DeleteMapping("/sku/{id}")
    public Result<Void> deleteSku(@PathVariable Long id) {
        productService.deleteSku(id);
        return Result.success();
    }

    @Operation(summary = "查询 SPU 下 SKU 列表")
    @GetMapping("/spu/{spuId}/skus")
    public Result<List<SkuVO>> listSkus(@PathVariable Long spuId) {
        return Result.success(productService.listSkusBySpuId(spuId));
    }
}
