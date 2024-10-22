package com.bbs.auth.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SystemState {

    NORMAL(0, "正常"),
    ERROR(1, "隐藏（page 类型资源）")
    ;

    private final int code;

    private final String description;

    public static final Map<Integer, SystemState> enumMap =
            EnumUtil.getEnumMap(SystemState.class).values().stream()
                    .collect(Collectors.toMap(SystemState::getCode, item -> item));
}
