package com.ds.mall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ds.mall.admin.dto.AdminOverviewQuery;
import com.ds.mall.admin.service.AdminOverviewService;
import com.ds.mall.admin.vo.AdminOverviewVO;
import com.ds.mall.order.entity.OrderInfo;
import com.ds.mall.order.mapper.OrderInfoMapper;
import com.ds.mall.product.entity.ProductSku;
import com.ds.mall.product.entity.ProductSpu;
import com.ds.mall.product.mapper.ProductSkuMapper;
import com.ds.mall.product.mapper.ProductSpuMapper;
import com.ds.mall.user.entity.UserAccount;
import com.ds.mall.user.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminOverviewServiceImpl implements AdminOverviewService {

    private final UserAccountMapper userAccountMapper;
    private final ProductSpuMapper spuMapper;
    private final ProductSkuMapper skuMapper;
    private final OrderInfoMapper orderInfoMapper;

    @Override
    public AdminOverviewVO overview(AdminOverviewQuery query) {
        AdminOverviewVO vo = new AdminOverviewVO();
        vo.setUserCount(userAccountMapper.selectCount(new LambdaQueryWrapper<UserAccount>()));
        vo.setSpuCount(spuMapper.selectCount(new LambdaQueryWrapper<ProductSpu>()));
        vo.setSkuCount(skuMapper.selectCount(new LambdaQueryWrapper<ProductSku>()));
        vo.setOrderCount(orderInfoMapper.selectCount(new LambdaQueryWrapper<OrderInfo>()));
        vo.setPendingOrderCount(orderInfoMapper.selectCount(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderStatus, "PENDING_PAYMENT")));
        return vo;
    }
}
