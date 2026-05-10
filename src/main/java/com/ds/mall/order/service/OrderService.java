package com.ds.mall.order.service;

import com.ds.mall.common.result.PageResult;
import com.ds.mall.order.dto.CreateOrderRequest;
import com.ds.mall.order.dto.OrderPageQuery;
import com.ds.mall.order.vo.OrderCreateVO;
import com.ds.mall.order.vo.OrderVO;

public interface OrderService {

    OrderCreateVO createOrder(CreateOrderRequest request);

    OrderVO detail(String orderNo);

    PageResult<OrderVO> myOrders(OrderPageQuery query);

    PageResult<OrderVO> adminPage(OrderPageQuery query);

    void cancel(String orderNo);

    void pay(String orderNo);

    void ship(String orderNo);

    void confirm(String orderNo);
}
