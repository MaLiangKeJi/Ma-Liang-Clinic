package com.clinic.cache.pay.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.app.AppPayService;
import com.clinic.cache.pay.PayCache;
import com.clinic.dto.PayRecordPatientDto;
import com.clinic.dto.param.PatientPayRecordParam;
import com.clinic.dto.param.UpdatePayById;
import com.clinic.entity.Dossier;
import com.clinic.entity.Prescription;
import com.clinic.enums.RedisKeys;
import com.clinic.util.LoginUser;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PayCacheImpl implements PayCache {
    private final AppPayService service;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Override
    public Result<Page<PayRecordPatientDto>> selectPayPatient(PatientPayRecordParam param) throws InterruptedException {
        Long id = LoginUser.getId();
        int tryNum = 3;
        while (tryNum > 0) {
            Object resourceStr = redis.opsForHash().get(getKey(id),JSONUtil.toJsonPrettyStr(param));
            if(Objects.nonNull(resourceStr)) {
                return JSONUtil.toBean(resourceStr.toString(), new TypeReference<Result<Page<PayRecordPatientDto>>>() {}, true);
            }

            if(tryAcquire(id)) {
                Result<Page<PayRecordPatientDto>> result = service.selectPayPatient(param);
                redis.opsForHash().put(getKey(id),JSONUtil.toJsonPrettyStr(param),JSONUtil.toJsonPrettyStr(result));
                if(!tryRelease(id)) {
                    log.error("尝试删除分布式锁失败！key={}", getLockKey(id));
                    throw new RedisException("尝试删除分布式锁失败！key=" + getLockKey(id));
                }
                return result;
            }
            tryNum--;
            wait(300);
        }
        return service.selectPayPatient(param);
    }

    @Override
    public Long createPayAndPrescriptionRecord(Prescription save, Dossier dossier) {
        Long id = LoginUser.getId();
        Long payId = service.createPayAndPrescriptionRecord(save, dossier);
        if(hasKey(getKey(id))){
            if(!deleteKey(getKey(id))) {
                log.error("尝试删除pay数据失败！key={}", getKey(id));
                throw new RedisException("尝试删除pay数据失败！key=" + getKey(id));
            }
        }
        return payId;
    }

    @Override
    public boolean updatePayById(UpdatePayById param) {
        Long id = LoginUser.getId();
        boolean update = service.updatePayById(param);
        if(hasKey(getKey(id))){
            if(!deleteKey(getKey(id))) {
                log.error("尝试删除pay数据失败！key={}", getKey(id));
                throw new RedisException("尝试删除分布式锁失败！key=" + getKey(id));
            }
        }
        return update;
    }


    private String getLockKey(Long uid) {
        return RedisKeys.LOG_PAY.lockKey(uid);
    }

    private String getKey(Long uid) {
        return RedisKeys.LOG_PAY.key(uid);
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
    public PayCacheImpl(AppPayService service) {
        this.service = service;
    }

}
