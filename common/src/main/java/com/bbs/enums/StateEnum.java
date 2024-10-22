package com.bbs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateEnum {

    USABLE(0, "正常可用"),
    ENABLE(1, "异常不可用");

    private final Integer code;

    private final String msg;

}
