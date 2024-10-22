package com.bbs.auth.service;

import com.bbs.auth.entity.User;
import com.bbs.vo.UserVO;
import com.bbs.exception.ReLoginException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

public interface TokenService {

    String getTokenKey(Long uid);

    UserVO verify(String token) throws ReLoginException;

    Boolean verifyToken(String token) throws ReLoginException;

    UserVO parseToken(String token);


    String createToken(User user);
    /**
     * 设置登录标识
     * @param uid 用户 ID（主键）
     */
    void setLoginFlag(Long uid);

    void setLoginFlag(Long uid, Integer expireNumber, TimeUnit expireUnit);

    String getLoginFlag(Long uid);

    /**
     * 清除登录标识
     * @param uid 用户 ID（主键）
     */
    void clearLoginFlag(Long uid);

    String getToken(HttpServletRequest request) throws ReLoginException;

    String verifyAndExpireToken(User user);
}
