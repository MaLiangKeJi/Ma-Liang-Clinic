package com.bbs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultStateEnum {

    NOT_DEFAULT(0, "非默认"),

    DEFAULT(1, "默认");


    private final Integer code;

    private final String msg;
}
