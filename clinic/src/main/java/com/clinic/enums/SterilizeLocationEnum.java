package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum SterilizeLocationEnum {

    STERILIZELOCATIONENUM(0, "听诊器"),
    BED(1, "诊床"),
    CLINICALWASTEPAIL(2, "医疗废物桶"),
    DOORKNOB(3, "门把手"),
    WALL(4, "墙面"),
    DESK(5, "诊桌"),
    SPHYGMOMANOMETER(6, "血压计"),
    PHARMACY(7, "药房"),
    CONSULTINGROOM(8, "诊室"),
    OBSERVATIONROOM(9, "观察室"),
    ;

    private final Integer code;

    private final String msg;

    public static final Map<String, SterilizeLocationEnum> map = EnumUtil.getEnumMap(SterilizeLocationEnum.class);
}
