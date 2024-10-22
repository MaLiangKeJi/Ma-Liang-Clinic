package com.clinic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DrugExpiryStateEnum {

    UNDEFINED(-1, "未定义"),
    NORMAL(0, "正常"),
    ABOUT_EXPIRES(1, "即将过期"),
    EXPIRES(2, "过期");

    private final Integer code;

    private final String msg;
}
