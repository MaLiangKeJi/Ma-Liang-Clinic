package com.bbs.auth.cache.impl;

import com.bbs.auth.cache.UserPermissionCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
public class UserPermissionCacheImpl implements UserPermissionCache {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    /**
     * 缓存 key 前缀
     */
    public static final String KEY_PREFIX = "USER_PERMISSION_";

    public String getKey(String suffix) {
        return KEY_PREFIX + suffix;
    }

    @Override
    public void remove(Long uid) {
        redis.delete(getKey(uid.toString()));
    }
}
