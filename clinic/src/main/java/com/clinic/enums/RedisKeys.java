package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Redis 缓存 keys
 * <a href="https://developer.aliyun.com/article/531067">规范参考：阿里云</a>
 */
@Getter
@AllArgsConstructor
public enum RedisKeys {

    USER_BUSINESS_BY_ID("user:id:business:", "营业实例"),

    UNIT("unit:", "单位表缓存"),
    LOG_ADMISSION("log:admission:", "门诊日志表缓存"),
    LOG_PAY("log:pay:", "支付记录缓存"),
    PRESCRIPTION("prescription:", "病历信息缓存"),
    PRESCRIPTION_ID_MAP("prescription:map:id", "病历ID与数据映射缓存"),
    USAGE("drug:sig:", "药品用法缓存"),
    STOCK_DRUG("stock:drug:", "库存药品缓存"),
    STOCK_DRUG_NAME("stock:drug:", "库存药品名称 & ID 映射缓存"),
    AUXILIARY_TYPE("auxiliary_type:", "辅助类型缓存"),
    INFORM("inform:", "通知缓存"),
    INFORM_USER("inform:user:", "通知用户缓存"),

    DRUG_OPEN_BY_ID("drug:open:uid:", "药房开药缓存"),
    ;

    private final String prefix;

    private final String description;

    private static final String LOCK_SUFFIX = "lock";

    /**
     * key
     * @param mark 业务标识 / 表名 / 主键
     * @return key
     */
    public String key(String mark) {
        return prefix + mark;
    }
    public String key(Long mark) {
        return key(mark.toString());
    }
    public String key(Integer mark) {
        return key(mark.toString());
    }
    /**
     * 锁 key
     * @param mark 业务标识 / 表名 / 主键
     * @return key
     */
    public String lockKey(String mark) {
        return prefix + mark + ":" + LOCK_SUFFIX;
    }
    public String lockKey(Long mark) {
        return lockKey(mark.toString());
    }
    public String lockKey() {
        return prefix + LOCK_SUFFIX;
    }

    public static final Map<String, RedisKeys> map = EnumUtil.getEnumMap(RedisKeys.class);
}
