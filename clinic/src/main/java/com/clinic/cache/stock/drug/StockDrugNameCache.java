package com.clinic.cache.stock.drug;

import com.clinic.entity.StockBatch;
import com.clinic.util.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.clinic.enums.RedisKeys.STOCK_DRUG_NAME;

@Component
public class StockDrugNameCache {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Resource
    private StockDrugCache stockDrugCache;

    public String key(String drugName) {
        return STOCK_DRUG_NAME.key(LoginUser.getId() + ":" + drugName);
    }


    public void set(String drugName, Long stockBatchId) {
        redis.opsForValue().set(key(drugName), stockBatchId.toString());
    }

    public void set(StockBatch stockBatch) {
        set(stockBatch.getName(), stockBatch.getId());
    }

    public Long get(String drugName) {
        String str = redis.opsForValue().get(key(drugName));
        return StringUtils.isNotBlank(str) ? Long.valueOf(str) : null;
    }

    public void setStockBatch(StockBatch stockBatch) {
        stockDrugCache.set(stockBatch);
        set(stockBatch);
    }
}
