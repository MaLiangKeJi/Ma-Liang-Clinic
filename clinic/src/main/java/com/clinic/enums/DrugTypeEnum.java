package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DrugTypeEnum {

    H(0, "化学药品"),
    Z(1, "中药"),
    S(2, "生物制品"),
    B(3, "保健药品(通过国家药品监督管理局整顿)"),
    T(4, "体外化学诊断试剂"),
    F(5, "药用辅料"),
    J(6, "进口分包装药品");

    private Integer code;

    private String msg;

    public static final Map<String, DrugTypeEnum> map = EnumUtil.getEnumMap(DrugTypeEnum.class);
}
