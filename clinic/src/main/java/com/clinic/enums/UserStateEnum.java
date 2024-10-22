package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserStateEnum {
    STATUS_NORMAL(0,"正常"),
    STATUS_LOCK(1,"冻结"),
    STATUS_BAN(-1,"封禁");

    private final Integer code;
    private final String msg;

    public static final Map<Integer, UserStateEnum> enumMap =
            EnumUtil.getEnumMap(UserStateEnum.class).values().stream()
            .collect(Collectors.toMap(UserStateEnum::getCode, item -> item));
}
