package com.ds.mall.product.controller;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.Result;
import com.ds.mall.product.dto.BrandSaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.service.BrandService;
import com.ds.mall.product.vo.BrandVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台品牌管理")
@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
public class AdminBrandController {

    private final BrandService brandService;

    @Operation(summary = "新增品牌")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody BrandSaveRequest request) {
        return Result.success(brandService.create(request));
    }

    @Operation(summary = "修改品牌")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody BrandSaveRequest request) {
        brandService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "删除品牌")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return Result.success();
    }

    @Operation(summary = "启用或禁用品牌")
    @PutMapping("/{id}/enabled")
    public Result<Void> updateEnabled(@PathVariable Long id, @RequestParam Integer enabled) {
        brandService.updateEnabled(id, enabled);
        return Result.success();
    }

    @Operation(summary = "查询品牌详情")
    @GetMapping("/{id}")
    public Result<BrandVO> detail(@PathVariable Long id) {
        return Result.success(brandService.detail(id));
    }

    @Operation(summary = "分页查询品牌")
    @GetMapping("/page")
    public Result<PageResult<BrandVO>> page(@ParameterObject PageQuery query) {
        return Result.success(brandService.page(query));
    }
}
