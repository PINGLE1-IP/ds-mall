package com.ds.mall.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单分页查询")
public class OrderPageQuery {

    private Long current = 1L;
    private Long size = 10L;
    private Long userId;
    private String orderNo;
    private String orderStatus;
    private String payStatus;
}
