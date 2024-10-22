package com.clinic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockStateEnum {

    UNDEFINED(-1, "未定义"),
    NORMAL(0, "正常"),
    SHORTAGE(1, "短缺");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String msg;
}
