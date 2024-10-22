package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum PeriodUnitEnum {

    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    YEAR(4, "年"),
    ;

    private final Integer code;

    private final String msg;

    public static final Map<String, SterilizeLocationEnum> map = EnumUtil.getEnumMap(SterilizeLocationEnum.class);
}
