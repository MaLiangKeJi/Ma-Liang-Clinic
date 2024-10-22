package com.bbs.util;

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PhoneUtil {

    /**
     * 手机号长度
     */
    private static final Integer PHONE_MAX_LENGTH = 11;

    /**
     * 手机号全数字匹配正则
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * 字符串是否是全数字
     * @param str 字符串
     * @return 是否是全数字
     */
    public static boolean isNumber(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 校验格式：手机号
     * @param phone 手机号
     * @throws IllegalArgumentException 手机号格式异常
     */
    public static void checkPhoneFormatThrows(String phone) throws IllegalArgumentException {
        checkArgument(isNoneBlank(phone) && PHONE_MAX_LENGTH == phone.length() && isNumber(phone), "手机号格式异常");
    }

    public static Boolean checkPhoneFormat(String phone) throws IllegalArgumentException {
        return isNoneBlank(phone) && PHONE_MAX_LENGTH == phone.length() && isNumber(phone);
    }

    /**
     * 校验格式：验证码
     * @param code 验证码
     * @throws IllegalArgumentException 验证码格式异常
     */
    public static void checkPhoneCodeFormat(String code) throws IllegalArgumentException {
        checkArgument(isNotBlank(code) && isNumber(code), "验证码格式异常");
        int intCode = Integer.parseInt(code);
        checkArgument(intCode >= 1000 && intCode <= 9999, "验证码格式异常");
    }
}
