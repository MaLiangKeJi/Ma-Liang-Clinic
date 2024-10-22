package com.clinic.cache.unit.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.clinic.cache.unit.UnitCache;
import com.clinic.entity.Unit;
import com.clinic.enums.RedisKeys;
import com.clinic.service.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class UnitCacheImpl implements UnitCache, ApplicationListener<ContextRefreshedEvent> {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Resource
    private UnitService service;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        reload();
        log.info("加载单位表（unit.table）数据到 Redis!");
    }

    @Override
    public List<String> getUnitStrList(List<String> ids) {
        List<String> unitStrList = getByRedis(ids);
        if(CollectionUtils.isEmpty(unitStrList)) {
            reload();
            return getByRedis(ids);
        }
        for (String unitStr : unitStrList) {
            if(Objects.isNull(unitStr)) {
                reload();
                return getByRedis(ids);
            }
        }
        return unitStrList;
    }

    @Override
    public Unit getUnit(Integer id) {
        return JSONUtil.toBean(getByRedis(generateKey(id)), Unit.class);
    }

    @Override
    public List<Unit> getUnit(List<Integer> ids) {
        List<String> keys = ids.stream().map(this::generateKey).collect(Collectors.toList());

        return getByRedis(keys)
                .stream().map(itemStr -> JSONUtil.toBean(itemStr, Unit.class)).collect(Collectors.toList());
    }

    @Override
    public List<Unit> getUnit(Set<Integer> ids) {
        return getUnit(new ArrayList<>(ids));
    }

    @Override
    public Map<Integer, Unit> getUnitMap(List<Integer> ids) {
        return getUnit(ids).stream().collect(Collectors.toMap(Unit::getId, unit -> unit));
    }

    @Override
    public Map<Integer, Unit> getUnitMap(Set<Integer> ids) {
        return getUnitMap(new ArrayList<>(ids));
    }

    private List<String> getByRedis(List<String> ids) {
        return redis.opsForValue().multiGet(ids);
    }

    private String getByRedis(String key) {
        return redis.opsForValue().get(key);
    }

    @Override
    public void reload() {
        if(tryAcquire()) {
            redis.opsForValue().multiSet(service.list().stream().collect(Collectors.toMap(unit -> generateKey(unit.getId()), JSONUtil::toJsonPrettyStr)));
            tryRelease();
        }
    }

    @Override
    public String generateKey(Integer id) {
        return RedisKeys.UNIT.key(id);
    }

    @Override
    public List<Unit> search(String name) {
        String key = RedisKeys.UNIT.key(name);
        String usageStr = redis.opsForValue().get(key);
        if(nonNull(usageStr)) {
            return JSONUtil.toBean(usageStr, new TypeReference<List<Unit>>() {
            }, true);
        }
        List<Unit> usageList = service.search(name);
        redis.opsForValue().set(key, JSONUtil.toJsonPrettyStr(usageList));
        return usageList;
    }

    /**
     * 尝试加锁（5分钟自动销毁）
     * @return 加锁结果
     */
    private Boolean tryAcquire() {
        return redis.opsForValue().setIfAbsent(RedisKeys.UNIT.lockKey(), Thread.currentThread().getName(), 1, TimeUnit.MINUTES);
    }

    private void tryRelease() {
        redis.delete(RedisKeys.UNIT.lockKey());
    }
}
