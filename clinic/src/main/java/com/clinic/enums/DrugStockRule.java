package com.clinic.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DrugStockRule implements IEnum<Integer> {

    NOT_COUNT(0, "不统计", "不纳入库存数量预警统计"),

    MIN_UNIT_PERCENTAGE_CUSTOMIZE(1, "自定义（最小单位）", "当库存数量不足【自定义阈值】时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）"),
    MIN_UNIT_PERCENTAGE_80(2, "80%（最小单位）", "当库存数量不足 80% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）"),
    MIN_UNIT_PERCENTAGE_50(3, "50%（最小单位）", "当库存数量不足 50% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）"),
    MIN_UNIT_PERCENTAGE_20(4, "20%（最小单位）", "当库存数量不足 50% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）");

    @JsonValue
    private final Integer code;

    private final String msg;

    private final String remark;

    public static List<DrugStockRule> list = Arrays.asList(
            NOT_COUNT,
            MIN_UNIT_PERCENTAGE_CUSTOMIZE,
            MIN_UNIT_PERCENTAGE_80,
            MIN_UNIT_PERCENTAGE_50,
            MIN_UNIT_PERCENTAGE_20
    );

    public static Map<Integer, DrugStockRule> map = list.stream().collect(Collectors.toMap(DrugStockRule::getCode, rule -> rule));

    @Override
    public Integer getValue() {
        return this.code;
    }
}
