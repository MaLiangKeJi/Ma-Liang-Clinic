package com.bbs.auth.cache;

import com.bbs.auth.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.bbs.auth.enums.RedisKeys.USER_LOGIN_COMPANY;

@Component
public class BindLoginCompanyCache {

    @Resource
    protected RedisUtil redisUtil;

    public String key(Long uid) {
        return USER_LOGIN_COMPANY.key(uid);
    }

    public void bind(Long uid, Long companyId) {
        redisUtil.set(key(uid), companyId.toString());
    }

    public Long get(Long uid) {
        String companyId = redisUtil.get(key(uid));
        return companyId == null ? null : Long.valueOf(companyId);
    }

    public void remove(Long uid) {
        redisUtil.delete(key(uid));
    }
}
