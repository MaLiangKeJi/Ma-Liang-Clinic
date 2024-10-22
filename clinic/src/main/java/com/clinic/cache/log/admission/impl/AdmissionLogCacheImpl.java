package com.clinic.cache.log.admission.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.cache.log.admission.AdmissionLogCache;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.dto.param.SearchAdmissionParam;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Patient;
import com.clinic.enums.RedisKeys;
import com.clinic.service.AdmissionLogService;
import com.clinic.util.LoginUser;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AdmissionLogCacheImpl implements AdmissionLogCache {


    private final AdmissionLogService database;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Override
    public Page<AdmissionLog> search(SearchAdmissionParam param) throws InterruptedException, ParseException {
        Long id = LoginUser.getId();
        int tryNum = 3;
        while (tryNum > 0) {
            Object resourceStr = redis.opsForHash().get(getKey(id),JSONUtil.toJsonPrettyStr(param));
            if(Objects.nonNull(resourceStr)) {
                return JSONUtil.toBean(resourceStr.toString(), new TypeReference<Page<AdmissionLog>>() {}, true);
            }

            if(tryAcquire(id)) {
                Page<AdmissionLog> page = database.search(param);
                redis.opsForHash().put(getKey(id),JSONUtil.toJsonPrettyStr(param), JSONUtil.toJsonPrettyStr(page));
                if(!tryRelease(id)) {
                    log.error("尝试删除分布式锁失败！key={}", getLockKey(id));
                    throw new RedisException("尝试删除分布式锁失败！key=" + getLockKey(id));
                }
                return page;
            }
            tryNum--;
            Thread.sleep(300);
        }
        return database.search(param);
    }

    @Override
    public void remove() {
        redis.delete(getKey(LoginUser.getId()));
    }


    @Override
    public Long save(RecordAdmissionLogParam param) {
        Long id = LoginUser.getId();
        Long save = database.save(param);
        if(hasKey(getKey(id))){
            if(!deleteKey(getKey(id))) {
                log.error("尝试删除门诊日志数据失败！key={}", getKey(id));
                throw new RedisException("尝试删除门诊日志数据失败！key=" + getKey(id));
            }
        }
        return save;
    }

    @Override
    public Long saveLogFormAddPatient(Patient patient) {
        Long id = LoginUser.getId();
        AdmissionLog admissionLog = new AdmissionLog(patient, id);
        database.save(admissionLog);
        if(hasKey(getKey(id))){
            if(!deleteKey(getKey(id))) {
                log.error("尝试删除门诊日志数据失败！key={}", getKey(id));
                throw new RedisException("尝试删除门诊日志数据失败！key=" + getKey(id));
            }
        }
        return admissionLog.getId();
    }


    private String getLockKey(Long uid) {
        return RedisKeys.LOG_ADMISSION.lockKey(uid);
    }

    private String getKey(Long uid) {
        return RedisKeys.LOG_ADMISSION.key(uid);
    }
    private Boolean deleteKey(String key){
        return redis.delete(key);
    }

    private Boolean hasKey(String key){
        return redis.hasKey(key);
    }

    /**
     * 尝试加锁（5分钟自动销毁）
     * @param uid UID
     * @return 加锁结果
     */
    private Boolean tryAcquire(Long uid) {
        return redis.opsForValue().setIfAbsent(getLockKey(uid), Thread.currentThread().getName(), 1, TimeUnit.MINUTES);
    }

    private Boolean tryRelease(Long uid) {
        return redis.delete(getLockKey(uid));
    }

    @Autowired
    public AdmissionLogCacheImpl(AdmissionLogService service) {
        this.database = service;
    }

}
