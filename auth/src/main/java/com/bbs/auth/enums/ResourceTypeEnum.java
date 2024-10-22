package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    PAGE(1L, "页面", "page"),
    APP(2L, "应用", "page"),
    USER_CONFIG(3L, "用户配置", "user_config"),
    ;

    private final Long type;

    private final String name;

    private final String code;
}
