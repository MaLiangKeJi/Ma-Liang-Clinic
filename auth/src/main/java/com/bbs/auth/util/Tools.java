package com.bbs.auth.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @Auther: hasee
 * @Date: 2021/3/25 17:04
 * @Description:
 */
public class Tools {

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String setNoncestr() {
        String str = UUID.randomUUID().toString().replaceAll("-", "");
        return str.substring(0, 16);
    }


    /**
     * sha1加密
     *
     * @param inStr
     * @return
     */
    public static String shaEncode(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = new byte[0];
        try {
            byteArray = inStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
