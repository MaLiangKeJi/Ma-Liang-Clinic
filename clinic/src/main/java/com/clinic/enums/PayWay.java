package com.clinic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收费方式：1微信，2支付宝，3挂账，4现金
 */
@Getter
@AllArgsConstructor
public enum PayWay {

    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝"),
    BANK(3, "挂账"),
    CASH(4, "现金"),
    ;

    @EnumValue
    private final Integer code;


    @JsonValue
    private final String msg;

}
