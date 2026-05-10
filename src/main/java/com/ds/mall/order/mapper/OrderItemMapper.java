package com.ds.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.mall.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    @Select("select * from order_item where order_id = #{orderId} and deleted = 0")
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
