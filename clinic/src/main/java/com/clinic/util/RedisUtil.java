package com.clinic.util;

import cn.hutool.core.lang.Opt;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    /**
     * 设置过期时间，单位秒
     * @param key 键的名称
     * @param timeout 过期时间
     * @return 成功：true，失败：false
     */
    public boolean setExpireTime(String key, long timeout) {
        return redis.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 通过键删除一个值
     * @param key 键的名称
     */
    public boolean delete(String key) {
        return redis.delete(key);
    }

    /**
     * 判断key是否存在
     * @param key 键的名称
     * @return 存在：true，不存在：false
     */
    public boolean hasKey(String key) {
        return redis.hasKey(key);
    }

    /**
     * 数据存储
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value) {
        redis.boundValueOps(key).set(value);
    }

    /**
     * 数据存储的同时设置过期时间
     * @param key 键
     * @param value 值
     * @param expireTime 过期时间
     */
    public void set(String key, String value, Long expireTime) {
        redis.boundValueOps(key).set(value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 数据取值
     * @param key 键
     * @return 查询成功：值，查询失败，null
     */
    public String get(String key) {
        return redis.boundValueOps(key).get();
    }

    /**
     * get
     * @param key 键
     * @return 为空则返回 Opt<Null>，不空返回 Opt
     */
    public Opt<String> getOpt(String key) {
        return Opt.ofNullable(redis.opsForValue().get(key));
    }

    public Long increment(String key) {
        return redis.boundValueOps(key).increment();
    }

    /**
     * 如果存在则设置
     * @param key 键
     * @return
     */
    public Boolean setIfPresent(String key, String value) {
        return redis.opsForValue().setIfPresent(key, value);
    }

    public void hashSet(String key, String hashKey, String value) {
        redis.opsForHash().put(key, hashKey, value);
    }

    public void hashSet(String key, Map<String, String> values) {
        redis.opsForHash().putAll(key, values);
    }

    public Opt<Map<String, String>> hashGet(String key) {
        Map<String, String> resultMap = new HashMap<>();

        Map<Object, Object> tmpMap = redis.opsForHash().entries(key);
        if (tmpMap.isEmpty())
            return Opt.ofNullable(resultMap);

        tmpMap.forEach((k, v) -> {
            resultMap.put(String.valueOf(k), String.valueOf(v));
        });

        return Opt.ofNullable(resultMap);
    }

    public Opt<String> hashGet(String key, String hashKey) {
        Object value = redis.opsForHash().get(key, hashKey);
        if (ObjectUtils.isEmpty(value))
            return Opt.empty();
        return Opt.of(String.valueOf(value));
    }

    public void multiSet(Map<String, String> map) {
        redis.opsForValue().multiSet(map);
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redis.expire(key, timeout, unit);
    }

    public boolean listLPush(String key, List<String> valueList) {
        Long size = redis.opsForList().leftPushAll(key, valueList.toArray(new String[valueList.size()]));
        if (size > NumberUtils.LONG_ZERO)
            return true;
        else
            return false;
    }

    public boolean listRPush(String key, List<String> valueList) {
        Long size = redis.opsForList().rightPushAll(key, valueList.toArray(new String[valueList.size()]));
        if (size > NumberUtils.LONG_ZERO)
            return true;
        else
            return false;
    }

    public List<String> listGet(String key) {
        ListOperations<String, String> tmpList = redis.opsForList();
        Long size = tmpList.size(key);
        List<String> resultList = tmpList.range(key, NumberUtils.INTEGER_ZERO, size - NumberUtils.INTEGER_ONE);

        if (ObjectUtils.isEmpty(resultList))
            return Collections.emptyList();
        return resultList;
    }

    /**
     * get
     *
     * @param key 键
     * @return 为空则返回 Opt<Null>，不空返回 Opt
     */
    public Opt<List<String>> getOptList(String key) {
        return Opt.ofNullable(listGet(key));
    }
}
