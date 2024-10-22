package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum DisinfectantEnum {

    chlorine(0, "含氯制剂（84消毒液、优氯净、益康等）"),
    alcohol(1, "75%以上酒精")
    ;

    private final Integer code;

    private final String msg;

    public static final Map<String, SterilizeLocationEnum> map = EnumUtil.getEnumMap(SterilizeLocationEnum.class);
}
