package com.clinic.cache.usage.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.clinic.cache.usage.UsageCache;
import com.clinic.entity.Usage;
import com.clinic.service.UsageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.clinic.enums.RedisKeys.USAGE;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class UsageCacheImpl implements UsageCache {

    private final UsageService service;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Override
    public List<Usage> search(String name) {
        String usageStr = redis.opsForValue().get(USAGE.key(name));
        if(nonNull(usageStr)) {
            return JSONUtil.toBean(usageStr, new TypeReference<List<Usage>>() {
            }, true);
        }
        List<Usage> usageList = service.search(name);
        redis.opsForValue().set(USAGE.key(name), JSONUtil.toJsonPrettyStr(usageList));
        return usageList;
    }

    @Autowired
    public UsageCacheImpl(UsageService service) {
        this.service = service;
    }
}
