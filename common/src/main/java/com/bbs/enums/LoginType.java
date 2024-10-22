package com.bbs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Objects.nonNull;

@Getter
@AllArgsConstructor
public enum LoginType {

    PHONE("phoneCode", "手机号"),
    PASSWORD("accountPassword", "密码"),
    WX("qrCode", "微信扫描二维码"),
    PASSWORD_CREATE("3", "手机号（未注册则直接注册）");

    private final String code;

    private final String msg;
}
