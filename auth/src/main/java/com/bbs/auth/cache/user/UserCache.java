package com.bbs.auth.cache.user;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.bbs.auth.conf.UserCacheConf;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.UserService;
import com.bbs.auth.util.RedisUtil;
import com.bbs.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.hutool.json.JSONUtil.toJsonPrettyStr;
import static com.bbs.auth.enums.RedisKeys.*;
import static com.bbs.auth.enums.RedisKeys.USER;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.*;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class UserCache {

    @Resource
    private RedisUtil redis;

    @Resource
    private RedisUtil.Redisson redissonUtil;

    @Resource
    private RedissonClient redisson;

    @Resource
    private UserService service;

    @Resource
    private UserDao db;

    @Resource
    private UserCacheConf conf;

    @Resource
    private UserCacheConf.PhoneMap phoneMapConf;

    @Lazy
    @Resource
    private WXOpenIDCache wxOpenIdCache;

    @Lazy
    @Resource
    private PhoneCache phoneCache;


    @Retryable(value = RestClientException.class, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public void set(User user) {
        redis.set(USER.key(user.getId()), toJsonPrettyStr(user), RandomUtil.randomInt(1, 5), TimeUnit.MINUTES);
    }

    public void remove(Long userId) {
        redis.delete(USER.key(userId));
    }

    public User get(Long uid) {
        return redis.get(USER.key(uid), User.class);
    }

    public List<User> get(List<Long> ids) {
        List<String> idStrList = ids.stream().filter(Objects::nonNull).map(USER::key).collect(Collectors.toList());
        return redis.multiGet(idStrList)
                .stream().map(str -> nonNull(str) ? JSONUtil.toBean(str, User.class) : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Retryable(value = RestClientException.class, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public void setUserAndPhoneMap(User user) {
        String userCacheKey = conf.key(user);
        String phoneMapKey = phoneMapConf.key(user);
        redis.multiSet(new HashMap<String, String>() {{
            put(userCacheKey, toJsonPrettyStr(user));
            put(phoneMapKey, user.getId().toString());
        }});
        expire(userCacheKey);
        phoneCache.expire(phoneMapKey);
    }

    public void expire(String userCacheKey) {
        redis.expire(userCacheKey, RandomUtil.randomInt(conf.minTimeout(), conf.maxTimeout()), MINUTES);
    }


    public User search(Long uid) throws InterruptedException, IllegalArgumentException {
        User user = redis.get(USER.key(uid), User.class);
        if(nonNull(user)) {
            return user;
        } else {
            return RedisUtil.Redisson.lockExec(redisson.getSpinLock(USER.LOCK.key(uid)), 500, 1000, MILLISECONDS, () ->
                    load(uid, 7, DAYS)
            );
        }
    }

    public User search(String email) throws InterruptedException, IllegalArgumentException {
        RLock lock = redisson.getSpinLock(USER.LOCK.key(email));
        String uid = searchUidFromRedis(email);
        if(uidMapIsExist(uid)) {
            // 有 email，无用户信息
            return RedisUtil.Redisson.lockExec(lock, 500, 1000, MILLISECONDS, () ->
                    load(Long.valueOf(uid), 7, DAYS)
            );
        } else {
            // 都没有
            return RedisUtil.Redisson.lockExec(lock, 500, 1000, MILLISECONDS, () -> {
                User user = searchUserFromDBElseThrow(email);
                setUserAndEmailMapToRedis(user);
                return user;
            });
        }
    }

    public static Boolean uidMapIsExist(String str) {
        return isNotBlank(str);
    }

    private String searchUidFromRedis(String email) {
        return redis.getOpt(USER_EMAIL_AND_ID_MAP.key(email)).get();
    }

    private void setUserAndEmailMapToRedis(User user) {
        redis.multiSet(new HashMap<String, String>() {{
            put(USER_EMAIL_AND_ID_MAP.key(user.getEmail()), String.valueOf(user.getId()));
            put(USER.key(user.getId()), toJsonPrettyStr(user));
        }});
    }

    private User searchUserFromDBElseThrow(String email) {
        return service.searchByEmailElseThrow(email);
    }

    public void updateByID(User user) throws BusinessException {
        Long uid = user.getId();

        RedisUtil.Redisson.lockExec(redisson.getSpinLock(USER.LOCK.key(uid)), 500, 1000, MILLISECONDS, () -> {
            if(service.updateById(user)) {
                load(service.getById(uid), 7, DAYS);
                return;
            }
            throw new BusinessException("修改用户信息以及缓存失败！");
        });
    }

    public User searchOrRegisterByPhone(Long phone) throws IllegalArgumentException {
        return redissonUtil.lockAlwaysExec(() -> {
                    Long uid = phoneCache.get(phone);
                    User user;
                    if(nonNull(uid)) {
                        String userCacheKey = USER.key(uid);
                        user = redis.get(userCacheKey, User.class);
                        if(isNull(user)) {  //长时间未登录，缓存的用户信息被删除，需要重新设置
                            user = db.searchByID(uid);
                            redis.set(userCacheKey, user, RandomUtil.randomInt(conf.minTimeout(), conf.maxTimeout()), MINUTES);
                        }
                        return user;
                    } else {
                        // 缓存无法查询，按 phone 从数据库查询，加载并重置缓存中的映射和用户信息
                        user = db.selectByPhone(phone);
                        if(nonNull(user)) {
                            setUserAndPhoneMap(user);
                        } else {
                            // 注册
                            user = service.registerByPhoneNoLockAndNoLoadCache(phone);
                            setUserAndPhoneMap(user);
                        }
                    }
                    return user;
                },
                redisson.getSpinLock(USER_PHONE_AND_ID_MAP.LOCK.key(phone)),
                50000,
                50000,
                MILLISECONDS
        );
    }

    public User searchOrRegisterByPhone(String phone) throws IllegalArgumentException {
        checkArgument(isNoneBlank(phone) && phone.length() == 11);
        return searchOrRegisterByPhone(Long.valueOf(phone));
    }

    /**
     * 通过手机号查询用户（需要加锁！！！！）
     * @param phone 手机号
     * @return 用户/NULL
     */
    public User searchByPhoneNoLockNoLoad(String phone) {
        try {
            Long uid = phoneCache.get(Long.valueOf(phone));
            User user;
            if (nonNull(uid)) {
                user = get(uid);
            } else {
                user = db.selectByPhone(phone);
            }
            return user;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void expireUserAndPhoneMap(User user) {
        // 设置用户信息的过期时间
        String userCacheKey = conf.key(user.getId());
        expire(userCacheKey);
        // 设置用户手机号映射的过期时间
        String phoneMapKey = phoneMapConf.key(user.getPhone());
        phoneCache.expire(phoneMapKey);

    }

    /**
     * 加载用户信息到缓存
     * @param uid 用户 ID（主键）
     * @param timeout 超时时间
     * @param unit 超时时间单位
     * @return 用户信息
     * @throws IllegalArgumentException 对应 ID 用户不存在！
     */
    public User load(Long uid, int timeout, TimeUnit unit) throws IllegalArgumentException {
        User user = db.searchByID(uid);
        redis.set(USER.key(uid), user, timeout, unit);
        return user;
    }

    public User load(Long uid) throws IllegalArgumentException {
        return db.searchByID(uid);
    }

    public void load(User user, int timeout, TimeUnit unit) {
        redis.set(USER.key(user.getId()), user, timeout, unit);
    }

    public static Boolean cacheIsExists(Long uidCache) {
        return nonNull(uidCache);
    }

    public static Boolean notRegistered(User user) throws BusinessException {
        if(nonNull(user)) {
            throw new BusinessException("修改用户手机号失败：缓存UID用户，查询数据库不存在");
        }
        return true;
    }
}
