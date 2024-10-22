package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum AISearchSearchKeyword {


    ADMISSIONS("接诊"),
    PATIENT("患者", "病患", "病人", "病号", "顾客", "看病"),
    STOCK("库存"),
    PRESCRIPTION("处方"),
    RETAIL("零售"),
    RETAIL_RECORD("零售"),
    STOCK_EXPIRED("过期", "临期"),
    STOCK_UNDER("不足", "不够", "库存不足", "缺货"),
    STERILIZE("消毒"),
    DISINFECTION("消杀"),
    ;

    private final List<String> keywords;

    public static final List<String> allKeywords = EnumUtil.getFieldValues(AISearchSearchKeyword.class, "keywords")
            .stream().map(obj -> (String)obj).collect(Collectors.toList());

    public static final Map<String, AISearchSearchKeyword> allMap = EnumUtil.getEnumMap(AISearchSearchKeyword.class);

    AISearchSearchKeyword(String... msg) {
        this.keywords = Arrays.asList(msg);
    }
}
