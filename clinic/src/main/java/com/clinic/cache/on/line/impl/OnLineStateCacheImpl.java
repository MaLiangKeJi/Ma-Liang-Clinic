package com.clinic.cache.on.line.impl;

import com.clinic.cache.on.line.OnLineStateCache;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class OnLineStateCacheImpl implements OnLineStateCache {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void setOnLine(Long id, Boolean onLine) {
        redisTemplate.opsForValue().set(key(id), onLine ? 1 : 0);
    }

    @Override
    public Boolean isOnLine(Long id) {
        boolean ret = false;
        Integer i = redisTemplate.opsForValue().get(key(id));
        if (ObjectUtils.isNotEmpty(i) && i == 1) {
            ret = true;
        }
        return ret;
    }


    public String key(Long id) {
        return "on_line_state:userId:" + id;
    }
}
