package com.bbs.auth.cache.code;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.bbs.auth.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;

import static com.bbs.auth.enums.RedisKeys.USER_PHONE_CODE;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Component
public class PhoneCodeCache {

    @Resource
    protected RedisUtil redisUtil;

    public Integer timeout() {
        return 50000;
    }

    public Boolean checkCode(Long phone, Integer code) {
        if(code > 999 && code <= 9999) {
            Integer serverPhoneCode = getCode(phone);
            return nonNull(serverPhoneCode) && serverPhoneCode.equals(code);
        }
        return false;
    }

    /**
     * 清除所有该手机号申请的验证码
     * @param phone 手机号
     */
    public void delCode(String phone) {
        String key = key(phone);
        redisUtil.delete(key);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Val {

        Integer code;

        Date sendTime;

        public Val(Integer code) {
            this.code = code;
            this.sendTime = new Date();
        }

        public String toJSONStr() {
            return JSONUtil.toJsonPrettyStr(this);
        }
    }

    public Integer getCode(Long phone) {
        return getCode(String.valueOf(phone));
    }

    public Integer getCode(String phone) {
        Val val = get(String.valueOf(phone));
        return nonNull(val) ? get(phone).getCode() : null;
    }

    public Val get(String phone) {
        String key = key(phone);
        String valStr = redisUtil.get(key);
        return nonNull(valStr) ? JSONUtil.toBean(valStr, Val.class) : null;
    }

    /**
     * 设置验证码（同个手机号，重复设置新码，会覆盖旧码）
     * @param phone 手机号
     * @param code 验证码
     */
    public void setCode(String phone, Integer code) {
        String key = key(phone);
        redisUtil.set(key, new Val(code).toJSONStr(), timeout(), MINUTES);
    }

    private String key(String phone) {
        return USER_PHONE_CODE.key(phone);
    }

    public void checkIsCanSendCode(Integer phone) throws IllegalArgumentException {
        checkArgument(nonNull(phone), "验证码未发送或已过期，请重新发送");
        checkIsCanSendCode(phone.toString());
    }
    public void checkIsCanSendCode(String phone) throws IllegalArgumentException {
        checkArgument(nonNull(phone), "验证码未发送或已过期，请重新发送");
        Val val = get(phone);
        if(nonNull(val)) {
            checkArgument(DateUtil.between(val.getSendTime(), new Date(), DateUnit.SECOND) >= 60, "频繁发送验证码，请等待");
        }
    }
}
