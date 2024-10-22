package com.clinic.service;

import java.util.Map;

public interface WeiXinLoginService {

    Map<String, Object> checkPhone(String website, String ticket, Long phone, boolean isEnd);
}