package com.clinic.cache.stock.drug;

import cn.hutool.json.JSONUtil;
import com.clinic.entity.StockBatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.clinic.enums.RedisKeys.STOCK_DRUG;

@Component
public class StockDrugCache {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    private String key(Long stockBatchId) {
        return STOCK_DRUG.key(stockBatchId);
    }


    public void set(StockBatch stockBatch) {
        redis.opsForValue().set(key(stockBatch.getId()), JSONUtil.toJsonPrettyStr(stockBatch), 30, TimeUnit.DAYS);
    }

    public StockBatch get(Long stockBatchId) {
        String str = redis.opsForValue().get(key(stockBatchId));
        return StringUtils.isNotBlank(str) ? JSONUtil.toBean(str, StockBatch.class, true) : null;
    }

    public void remove(Long stockBatchId) {
        redis.delete(key(stockBatchId));
    }
}
