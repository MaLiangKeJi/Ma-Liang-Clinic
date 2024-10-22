package com.bbs.auth.util.captcha;

import com.bbs.auth.cache.code.PhoneCodeCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 验证码工具
 */
@Slf4j
public abstract class CaptchaUtil {


    @Value("${code.alibaba.sign}")
    private String signName;

    @Value("${code.alibaba.template}")
    private String templateCode;

//    @Value("${code.feigeyun.sign}")
//    private String signName;
//
//    @Value("${code.feigeyun.template}")
//    private String templateCode;


    @Resource
    protected PhoneCodeCache cache;

    /**
     * 发送短信
     * @param phoneNumber 目标手机号
     * @param signName  签名名称
     * @param templateCode 模板 Code
     * @param templateParam 模板变量实际值（JSON）
     * @throws Exception 发送异常
     */
    public abstract void send(
            String phoneNumber,
            String signName,
            String templateCode,
            String templateParam
    ) throws Exception;

    public Boolean send(String phone) throws IllegalArgumentException {
        try {
        checkPhoneFormat(phone);
        cache.checkIsCanSendCode(phone);
        Integer code = createCode();
            send(phone, signName, templateCode,  "{\"code\":\"" + code +"\"}");
            cache.setCode(phone, code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Integer createCode() {
        return (int)((Math.random() *9 +1) *1000);
    }

    private void checkPhoneFormat(String phone) throws IllegalArgumentException {
        checkArgument(StringUtils.isNotBlank(phone) && phone.length() == 11, "手机号格式异常");
        for (int i = 0; i < phone.length(); i++) {
            if (phone.charAt(i) == ' ' || (phone.charAt(i) >= 97 &&
                    phone.charAt(i) <= 122) || (phone.charAt(i) >= 65 && phone.charAt(i) <= 90)) {
                throw new IllegalArgumentException("手机号格式异常");
            }
        }
    }
}
