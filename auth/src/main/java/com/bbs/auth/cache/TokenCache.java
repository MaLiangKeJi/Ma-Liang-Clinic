package com.bbs.auth.cache;

public interface TokenCache {

    void setToken(Long uid, String token);

    String getToken(Long uid);

    /**
     * 延长 Token 过期时间
     * @param uid 用户ID
     */
    void expireToken(Long uid);
}
