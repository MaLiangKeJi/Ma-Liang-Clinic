package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum SterilizeModeEnum {

    SPRAY(0, "喷洒"),
    WIPE(1, "擦拭")
    ;

    private final Integer code;

    private final String msg;

    public static final Map<String, SterilizeLocationEnum> map = EnumUtil.getEnumMap(SterilizeLocationEnum.class);
}
