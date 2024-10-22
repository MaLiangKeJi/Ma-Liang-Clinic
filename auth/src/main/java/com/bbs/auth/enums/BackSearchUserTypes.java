package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BackSearchUserTypes {

    ALL("all", "查询全部用户"),
    ON_LINE("onLine", "查询在线用户"),
    ;

    private final String code;

    private final String description;
}
