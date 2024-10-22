package com.bbs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {

    FAILED_BUSINESS(401, "业务异常"),

    FAILED_PARAM_NOT_AVAILABLE(400, "参数不可用"),

    FAILED_USER_CODE_NOT_AVAILABLE(400, "验证码错误"),

    FAILED_USER_INFO_DUPLICATION(400, "手机号已注册"),

    FAILED_USER_NOT_LOGIN(401, "用户未登录"),

    SUCCESS_USER_LOGIN(200,"用户登录成功"),

    FAILED_USER_LOGIN_EXPIRE(401, "请重新登录！"),

    FAILED_AUTH_PHONE_CODE_NOT_AVAILABLE(400, "短信验证码不可用"),

    FAILED_REG_INVITE_NOT_AVAILABLE(400,"邀请码已过期"),

    // Login
    //-----------------------------------------------------------------------------------------------------------------
    FAILED_LOGIN_TYPE_NOT_AVAILABLE(400, "登录类型不可用"),
    FAILED_LOGIN_USER_NOT_EXISTS(400, "账户或密码错误"),   //账号不存在
    FAILED_LOGIN_PWD_ERROR(400, "账户或密码错误"),
    FAILED_LOGIN_USER_STATUS_ERROR(400, "账户状态异常，请联系客服"),

    FAILED_LOGIN_USER_NEED_REGISTER(401, "账号不存在，请使用【短信验证码】或【微信扫码】进行注册"),
    FAILED_LOGIN_USER_NOT_SET_PWD(401, "未设置密码，请使用【短信验证码】登录"),

    FAILED_ACCOUNT_EXPIRED(4001, "套餐已过期，请续费"),
    ;


    private final Integer code;

    private final String msg;

    @Override
    public String toString() {
        return msg;
    }
}
