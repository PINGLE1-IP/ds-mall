package com.ds.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.mall.order.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("select * from order_info where order_no = #{orderNo} and deleted = 0 limit 1")
    OrderInfo selectByOrderNo(@Param("orderNo") String orderNo);
}
