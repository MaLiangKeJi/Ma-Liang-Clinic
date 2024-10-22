package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceStateEnum {

    NORMAL(0, "正常"),
    HIDE(1, "隐藏（page 类型资源）")
    ;

    private final int code;

    private final String description;
}
