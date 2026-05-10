package com.ds.mall.product.service;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.product.dto.ProductPageQuery;
import com.ds.mall.product.dto.SkuSaveRequest;
import com.ds.mall.product.dto.SpuSaveRequest;
import com.ds.mall.product.vo.SkuVO;
import com.ds.mall.product.vo.SpuVO;

import java.util.List;

public interface ProductService {

    Long createSpu(SpuSaveRequest request);

    void updateSpu(Long id, SpuSaveRequest request);

    void deleteSpu(Long id);

    SpuVO detail(Long id);

    PageResult<SpuVO> page(ProductPageQuery query);

    void onSale(Long id);

    void offSale(Long id);

    void auditPass(Long id);

    void auditReject(Long id);

    Long createSku(SkuSaveRequest request);

    void updateSku(Long id, SkuSaveRequest request);

    void deleteSku(Long id);

    List<SkuVO> listSkusBySpuId(Long spuId);
}
