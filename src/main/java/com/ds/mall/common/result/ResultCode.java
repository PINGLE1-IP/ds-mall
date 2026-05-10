package com.ds.mall.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "权限不足"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_DISABLED(1003, "用户已禁用"),
    PRODUCT_NOT_FOUND(2001, "商品不存在"),
    SKU_NOT_FOUND(2002, "SKU 不存在"),
    CATEGORY_NOT_FOUND(2003, "分类不存在"),
    STOCK_NOT_ENOUGH(3001, "库存不足"),
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态不正确"),
    REPEAT_OPERATION(4003, "重复操作"),
    SYSTEM_ERROR(500, "系统异常");

    private final Integer code;
    private final String message;
}
