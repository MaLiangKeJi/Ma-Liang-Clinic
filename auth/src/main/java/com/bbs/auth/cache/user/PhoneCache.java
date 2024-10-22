package com.bbs.auth.cache.user;

import cn.hutool.core.util.RandomUtil;
import com.bbs.auth.conf.UserCacheConf;
import com.bbs.auth.entity.User;
import com.bbs.auth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

import static cn.hutool.json.JSONUtil.toJsonPrettyStr;
import static com.bbs.auth.cache.user.UserCache.uidMapIsExist;
import static com.bbs.auth.enums.RedisKeys.USER;
import static com.bbs.auth.enums.RedisKeys.USER_PHONE_AND_ID_MAP;
import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Component
public class PhoneCache {

    @Resource
    private RedisUtil redis;

    @Resource
    private UserCacheConf conf;

    @Resource
    private UserCache cache;

    public Long get(String phone) {
        return get(Long.valueOf(phone));
    }
    public Long get(Long phone) {
        String uidStr = redis.getOpt(USER_PHONE_AND_ID_MAP.key(phone)).get();
        return uidMapIsExist(uidStr) ? Long.valueOf(uidStr) : null;
    }

    public void expire(String phoneMapKey) {
        redis.expire(phoneMapKey, RandomUtil.randomInt(conf.associationMapMinTimeout(), conf.associationMapMaxTimeout()), MINUTES);
    }

    public String phoneMapKey(String phone) {
        return conf.key(Long.valueOf(phone));
    }

    public Boolean reloadAndExpire(String newPhone, User user) {
        String userKey = USER.key(user.getId());
        Long oldPhone = user.getPhone();
        String newPhoneMapKey = phoneMapKey(newPhone);
        String oldPhoneMapKey = phoneMapKey(String.valueOf(oldPhone));
        user.setPhone(Long.valueOf(newPhone));

        redis.multiSet(new HashMap<String, String>() {{
            put(userKey, toJsonPrettyStr(user));
            put(newPhoneMapKey, user.getId().toString());
        }});

        redis.delete(oldPhoneMapKey);

        cache.expire(userKey);
        expire(newPhoneMapKey);

        return true;
    }

    public void remove(String phone) {
        redis.delete(USER_PHONE_AND_ID_MAP.key(phone));
    }
}
