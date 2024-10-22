package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存提醒：统计库存规则
 */
@Getter
@AllArgsConstructor
public enum StockStateCountTypeEnum {

    PERCENTAGE(0, "百分比"),
    NUMBER(1, "数量");

    private final Integer code;

    private final String msg;

    public static final Map<Integer, StockStateCountTypeEnum> map = EnumUtil.getEnumMap(StockStateCountTypeEnum.class)
            .values()
            .stream().collect(Collectors.toMap(StockStateCountTypeEnum::getCode, item -> item));
}
