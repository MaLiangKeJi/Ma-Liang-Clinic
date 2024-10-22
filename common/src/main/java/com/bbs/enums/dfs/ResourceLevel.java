package com.bbs.enums.dfs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceLevel {


    ZERO(0, "0级"),

    ONE(1, "1级"),

    TOW(2, "2级");


    private final Integer code;

    private final String msg;
}
