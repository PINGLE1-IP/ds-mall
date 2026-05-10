package com.ds.mall.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建订单请求")
public class CreateOrderRequest {

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemRequest> items;

    @NotBlank(message = "收货人不能为空")
    private String receiverName;

    @NotBlank(message = "收货手机号不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;
}
