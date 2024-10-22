package com.bbs.auth.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface WeiXinService {
    Map<String,String> getQrCode();

    String receive(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request) throws IOException;

    Map<String, Object> checkLogin(String ticket, Integer expireNumber);

    String createMenu();

}