package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DrugUnitTypeEnum {

    GRAIN(1, "粒"),
    TABLET(2, "片"),
    BRANCH(3, "支"),
    DOZEN(4, "板"),
    BOX(5, "盒"),
    BAG(6, "袋"),
    BOTTLE(7, "瓶"),
    CHEST(8, "箱");
    private Integer code;

    private String msg;

    public static final List<DrugUnitTypeEnum> list = getEnumList();

    private static List<DrugUnitTypeEnum> getEnumList() {
        return Arrays.asList(GRAIN, TABLET, BRANCH, DOZEN, BOX, BAG, BOTTLE, CHEST);
    }
}
