package com.ds.mall.product.service;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.product.dto.BrandSaveRequest;
import com.ds.mall.product.dto.PageQuery;
import com.ds.mall.product.vo.BrandVO;

public interface BrandService {

    Long create(BrandSaveRequest request);

    void update(Long id, BrandSaveRequest request);

    void delete(Long id);

    void updateEnabled(Long id, Integer enabled);

    BrandVO detail(Long id);

    PageResult<BrandVO> page(PageQuery query);
}
