package com.bbs.auth.cache.impl;

import cn.hutool.core.util.RandomUtil;
import com.bbs.auth.cache.TokenCache;
import com.bbs.auth.util.RedisUtil;
import com.bbs.auth.enums.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenCacheImpl implements TokenCache {

    @Resource
    private RedisUtil redis;

    private String key(Long uid) {
        return RedisKeys.USER_UID_AND_TOKEN_MAP.key(uid);
    }

    private Integer getTokenTimeOutMax() { return 10080; }

    private Integer getTokenTimeOutMin() { return 8640; }

    @Override
    public void setToken(Long uid, String token) {
        int timeout = RandomUtil.randomInt(getTokenTimeOutMin(), getTokenTimeOutMax());
        redis.set(key(uid), token, timeout, TimeUnit.MINUTES);
    }

    @Override
    public String getToken(Long uid) throws IllegalArgumentException {
        return redis.get(key(uid));
    }

    @Override
    public void expireToken(Long uid) {
        int timeout = RandomUtil.randomInt(getTokenTimeOutMin(), getTokenTimeOutMax());
        redis.expire(key(uid), timeout, TimeUnit.MINUTES);
    }
}
