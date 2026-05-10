package com.ds.mall.product.service;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.product.dto.CategorySaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    Long create(CategorySaveRequest request);

    void update(Long id, CategorySaveRequest request);

    void delete(Long id);

    void updateEnabled(Long id, Integer enabled);

    CategoryVO detail(Long id);

    List<CategoryVO> tree(boolean enabledOnly);

    PageResult<CategoryVO> page(PageQuery query);
}
