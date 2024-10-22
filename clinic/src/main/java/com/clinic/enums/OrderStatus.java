package com.clinic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 * 0 已创建。医生：待出价；药代：待出价
 * 1 未支付 医生：未支付；药代：待支付
 * 2 已取消，超时未支付或手动取消
 * 3 待发货。医生：待发货；药代：已支付，竞价结束，等待药代上传送货方式
 * 4 已发货，药代已上传送货方式
 * 5 确认收货
 * 6 待退款
 * 7 已退款
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    WAITING_BID(0, "已创建"),
    WAITING_PAY(1, "未支付"),
    CANCELED(2, "已取消"),
    WAITING_SEND(3, "待发货"),
    SHIPPED(4, "已发货"),
    CONFIRMED(5, "确认收货"),
    WAITING_REFUND(6, "待退款"),
    REFUNDED(7, "已退款")
    ;


    @EnumValue
    private final Integer code;
    @JsonValue
    private final String msg;


}
