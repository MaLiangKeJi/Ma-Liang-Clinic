package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    SALES(4, "销售"),
    ORDINARY(3, "普通用户"),
    DRUG_AGENCY(2, "药代"),
    ;

    private final Integer code;

    private final String description;
}
