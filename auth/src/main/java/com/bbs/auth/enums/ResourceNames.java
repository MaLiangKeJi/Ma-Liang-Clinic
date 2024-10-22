package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ResourceNames {

    @Getter
    @AllArgsConstructor
    public enum UserConfig {

        CLINIC_NAME("clinic_name", "诊所名称"),

        STOCK_COUNT_TYPE("stock_count_type", "库存统计类型"),
        STOCK_COUNT_VAL("stock_count_val", "库存统计值"),
        STOCK_COUNT_UNIT("stock_count_unit", "库存统计单位"),
        DRUG_EXPIRE_VAL("drug_expire_val", "药品过期预警时间"),
        DRUG_EXPIRE_UNIT("drug_expire_unit", "药品过期预警时间单位");

        private final String name;

        private final String description;
    }
}
