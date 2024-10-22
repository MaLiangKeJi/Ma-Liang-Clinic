package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    ADMIN("管理员", "Admin"),
    USER("用户", "Info"),
    CASUAL_USER("用户组（试用）", "CasualUser")
    ;

    private final String name;

    private final String code;
}
