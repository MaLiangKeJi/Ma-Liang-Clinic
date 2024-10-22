package com.bbs.auth.cache.user;

import cn.hutool.core.util.RandomUtil;
import com.bbs.auth.conf.UserCacheConf;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserBind;
import com.bbs.auth.entity.VXUser;
import com.bbs.auth.service.UserBindService;
import com.bbs.auth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import java.util.HashMap;

import static cn.hutool.json.JSONUtil.toJsonPrettyStr;
import static com.bbs.auth.cache.user.UserCache.uidMapIsExist;
import static com.bbs.auth.enums.RedisKeys.USER;
import static com.bbs.auth.enums.RedisKeys.USER_OPEN_ID_AND_ID_MAP;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Component
public class WXOpenIDCache {

    @Resource
    private RedisUtil redis;

    @Resource
    private UserCacheConf conf;

    @Resource
    private UserCacheConf.WXOpenIDMap wxOpenIDMapConf;

    @Lazy
    @Resource
    private UserCache cache;

    @Resource
    private RedisUtil.Redisson redissonUtil;

    @Resource
    private RedissonClient redisson;

    @Resource
    private UserDao db;

    @Resource
    private UserBindService userBindService;

    @Retryable(value = RestClientException.class, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public void setUserAndOpenIDMap(UserBind userBind) {
        User user = userBind.getUser();
        String userCacheKey = conf.key(user);
        String openIDMapKey = wxOpenIDMapConf.key(userBind);
        redis.multiSet(new HashMap<String, String>() {{
            put(userCacheKey, toJsonPrettyStr(user));
            put(openIDMapKey, user.getId().toString());
        }});
        cache.expire(userCacheKey);
        expire(openIDMapKey);
    }

    public void expire(String openIDMapKey) {
        redis.expire(openIDMapKey, RandomUtil.randomInt(conf.associationMapMinTimeout(), conf.associationMapMaxTimeout()), MINUTES);
    }

    public VXUser searchByOpenID(String openid) throws IllegalArgumentException {
        return redissonUtil.lockAlwaysExec(() -> {
                    Long uid = get(openid);
                    if(nonNull(uid)) {  // 微信已经绑定账号了
                        String key = USER.key(uid);
                        User user = redis.get(key, User.class);
                        if(isNull(user)) {  // 长时间未登录，需要重新加载数据到缓存
                            user = db.searchByID(uid);
                            redis.set(key, user, RandomUtil.randomInt(conf.minTimeout(), conf.maxTimeout()), MINUTES);
                        }
                        return new VXUser(user, openid);
                    } else {
                        UserBind userBind = userBindService.searchUserBind(openid);
                        checkArgument(nonNull(userBind), "微信未绑定账号，请绑定账号后重试");
                        //微信已经绑定账号了，但长时间未登录，导致缓存数据清除
                        setUserAndOpenIDMap(userBind);
                        return new VXUser(userBind.getUser(), openid);
                    }
                },
                redisson.getSpinLock(USER.LOCK.key(openid)),
                50000,
                50000,
                MILLISECONDS
        );
    }

    public Long get(String openid) {
        String uidStr = redis.getOpt(USER_OPEN_ID_AND_ID_MAP.key(openid)).get();
        return uidMapIsExist(uidStr) ? Long.valueOf(uidStr) : null;
    }
}
