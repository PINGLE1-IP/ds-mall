package com.ds.mall.product.controller;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.Result;
import com.ds.mall.product.dto.CategorySaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.service.CategoryService;
import com.ds.mall.product.vo.CategoryVO;
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

import java.util.List;

@Tag(name = "后台分类管理")
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody CategorySaveRequest request) {
        return Result.success(categoryService.create(request));
    }

    @Operation(summary = "修改分类")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategorySaveRequest request) {
        categoryService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @Operation(summary = "启用或禁用分类")
    @PutMapping("/{id}/enabled")
    public Result<Void> updateEnabled(@PathVariable Long id, @RequestParam Integer enabled) {
        categoryService.updateEnabled(id, enabled);
        return Result.success();
    }

    @Operation(summary = "查询分类详情")
    @GetMapping("/{id}")
    public Result<CategoryVO> detail(@PathVariable Long id) {
        return Result.success(categoryService.detail(id));
    }

    @Operation(summary = "查询后台分类树")
    @GetMapping("/tree")
    public Result<List<CategoryVO>> tree() {
        return Result.success(categoryService.tree(false));
    }

    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public Result<PageResult<CategoryVO>> page(@ParameterObject PageQuery query) {
        return Result.success(categoryService.page(query));
    }
}
