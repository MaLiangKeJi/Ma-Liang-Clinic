package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserConfigResourceNames {

    CLINIC_NAME("clinic_name", "诊所名称");

    private final String name;

    private final String description;
}
