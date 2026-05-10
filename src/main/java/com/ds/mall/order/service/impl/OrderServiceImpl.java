package com.ds.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.PageResult;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.security.SecurityUtils;
import com.ds.mall.common.util.BeanCopyUtils;
import com.ds.mall.common.util.IdGenerator;
import com.ds.mall.order.dto.CreateOrderRequest;
import com.ds.mall.order.dto.OrderItemRequest;
import com.ds.mall.order.dto.OrderPageQuery;
import com.ds.mall.order.entity.OrderInfo;
import com.ds.mall.order.entity.OrderItem;
import com.ds.mall.order.mapper.OrderInfoMapper;
import com.ds.mall.order.mapper.OrderItemMapper;
import com.ds.mall.order.service.OrderService;
import com.ds.mall.order.vo.OrderCreateVO;
import com.ds.mall.order.vo.OrderItemVO;
import com.ds.mall.order.vo.OrderVO;
import com.ds.mall.product.entity.ProductSku;
import com.ds.mall.product.entity.ProductSpu;
import com.ds.mall.product.mapper.ProductSkuMapper;
import com.ds.mall.product.mapper.ProductSpuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductSkuMapper skuMapper;
    private final ProductSpuMapper spuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateVO createOrder(CreateOrderRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        String orderNo = IdGenerator.orderNo();
        BigDecimal totalAmount = BigDecimal.ZERO;

        OrderInfo order = new OrderInfo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setOrderStatus("PENDING_PAYMENT");
        order.setPayStatus("TO_PAY");
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        orderInfoMapper.insert(order);

        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductSku sku = requireSku(itemRequest.getSkuId());
            ProductSpu spu = requireSpu(sku.getSpuId());
            if (!"ON_SALE".equals(spu.getSaleStatus()) || sku.getEnabled() != 1) {
                throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "商品当前不可购买");
            }
            if (skuMapper.lockStock(sku.getId(), itemRequest.getQuantity()) == 0) {
                throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
            }
            BigDecimal itemTotal = sku.getSalePrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setSkuId(sku.getId());
            item.setSkuName(sku.getName());
            item.setSkuImage(spu.getMainImage());
            item.setSalePrice(sku.getSalePrice());
            item.setQuantity(itemRequest.getQuantity());
            item.setTotalAmount(itemTotal);
            orderItemMapper.insert(item);
        }

        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        orderInfoMapper.updateById(order);

        OrderCreateVO vo = new OrderCreateVO();
        vo.setOrderNo(orderNo);
        vo.setPayAmount(totalAmount);
        return vo;
    }

    @Override
    public OrderVO detail(String orderNo) {
        OrderInfo order = requireOrder(orderNo);
        assertOwnerOrAdmin(order);
        return toOrderVO(order);
    }

    @Override
    public PageResult<OrderVO> myOrders(OrderPageQuery query) {
        query.setUserId(SecurityUtils.getCurrentUserId());
        return page(query);
    }

    @Override
    public PageResult<OrderVO> adminPage(OrderPageQuery query) {
        return page(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String orderNo) {
        OrderInfo order = requireOrder(orderNo);
        assertOwnerOrAdmin(order);
        if (!"PENDING_PAYMENT".equals(order.getOrderStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "仅待支付订单可取消");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        for (OrderItem item : items) {
            if (skuMapper.releaseLockedStock(item.getSkuId(), item.getQuantity()) == 0) {
                throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "释放锁定库存失败");
            }
        }
        order.setOrderStatus("CANCELLED");
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pay(String orderNo) {
        OrderInfo order = requireOrder(orderNo);
        assertOwner(order);
        if (!"PENDING_PAYMENT".equals(order.getOrderStatus()) || !"TO_PAY".equals(order.getPayStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单不可支付");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        for (OrderItem item : items) {
            if (skuMapper.deductLockedStock(item.getSkuId(), item.getQuantity()) == 0) {
                throw new BusinessException(ResultCode.STOCK_NOT_ENOUGH);
            }
        }
        order.setPayStatus("PAY_SUCCESS");
        order.setOrderStatus("PAY_SUCCESS");
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ship(String orderNo) {
        OrderInfo order = requireOrder(orderNo);
        if (!"PAY_SUCCESS".equals(order.getOrderStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "仅已支付订单可发货");
        }
        order.setOrderStatus("SHIPPED");
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String orderNo) {
        OrderInfo order = requireOrder(orderNo);
        assertOwner(order);
        if (!"SHIPPED".equals(order.getOrderStatus())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "仅已发货订单可确认收货");
        }
        order.setOrderStatus("FINISHED");
        orderInfoMapper.updateById(order);
    }

    private PageResult<OrderVO> page(OrderPageQuery query) {
        Page<OrderInfo> page = orderInfoMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(query.getUserId() != null, OrderInfo::getUserId, query.getUserId())
                        .eq(StringUtils.hasText(query.getOrderNo()), OrderInfo::getOrderNo, query.getOrderNo())
                        .eq(StringUtils.hasText(query.getOrderStatus()), OrderInfo::getOrderStatus, query.getOrderStatus())
                        .eq(StringUtils.hasText(query.getPayStatus()), OrderInfo::getPayStatus, query.getPayStatus())
                        .orderByDesc(OrderInfo::getCreatedAt));
        Page<OrderVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::toOrderVO).toList());
        return PageResult.of(voPage);
    }

    private OrderInfo requireOrder(String orderNo) {
        OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    private ProductSku requireSku(Long skuId) {
        ProductSku sku = skuMapper.selectById(skuId);
        if (sku == null) {
            throw new BusinessException(ResultCode.SKU_NOT_FOUND);
        }
        return sku;
    }

    private ProductSpu requireSpu(Long spuId) {
        ProductSpu spu = spuMapper.selectById(spuId);
        if (spu == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        return spu;
    }

    private void assertOwner(OrderInfo order) {
        if (!order.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    private void assertOwnerOrAdmin(OrderInfo order) {
        if (!SecurityUtils.isAdmin()) {
            assertOwner(order);
        }
    }

    private OrderVO toOrderVO(OrderInfo order) {
        OrderVO vo = BeanCopyUtils.copy(order, OrderVO.class);
        vo.setItems(BeanCopyUtils.copyList(orderItemMapper.selectByOrderId(order.getId()), OrderItemVO.class));
        return vo;
    }
}
