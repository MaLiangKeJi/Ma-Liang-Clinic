package com.clinic.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bbs.Result;
import com.bbs.api.auth.UserAPI;
import com.bbs.enums.CodeEnum;
import com.clinic.converter.SettingsConverter;
import com.clinic.dto.param.AddSettingsParam;
import com.clinic.dto.param.UpdateSettingsParam;
import com.clinic.entity.Settings;
import com.clinic.enums.RedisKeys;
import com.clinic.mapper.SettingsMapper;
import com.clinic.service.SettingsService;
import com.clinic.util.LoginUser;
import com.clinic.util.RedisUtil;
import com.clinic.util.log.LogUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 *
 */
@Service
public class SettingsServiceImpl extends MPJBaseServiceImpl<SettingsMapper, Settings>
    implements SettingsService {

    @DubboReference
    private UserAPI api;

    @Resource
    private SettingsConverter settingsConverter;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;

    private static final String SETTING_KEY_PREFIX = "SETTING_USER_ID=";

    @Value("${setting.stock.expiry.alert.month}")
    private Integer stockDefaultExpiryAlertMonth;

    @Override
    public Result<Boolean> add(AddSettingsParam param) {
        Settings settings = settingsConverter.toEntity(param);
        Long userId = LoginUser.getId();
        settings.setUserId(userId);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            settings.setInviteUid(api.getUidByInvite(param.getInviteCode()));
            if (Objects.nonNull(settings.getInviteUid())){
                Long uid = settings.getInviteUid();
                if (uid.equals(NumberUtils.LONG_MINUS_ONE))
                    return Result.failed(CodeEnum.FAILED_REG_INVITE_NOT_AVAILABLE.getMsg());
            }

            boolean save = save(settings);
            if(hasKey(getKey(userId))){
                //同步添加redis中的数据
                if(save)redis.opsForValue().set(getKey(userId), JSONUtil.toJsonPrettyStr(settings));
            }
            LogUtil.Operation.addClinicSetting("{}新增设置：设置Id={}", LoginUser.get().getName(), settings.getId());
            transactionManager.commit(transaction);
            return Result.success(true);
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            return Result.failed(400, e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result<Boolean> update(UpdateSettingsParam param) {
        Settings settings = settingsConverter.toEntity(param);
        Long userId = LoginUser.getId();
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        boolean b = updateById(settings);
        if(b){
            Settings dbSettings = lambdaQuery().eq(Settings::getUserId, userId).one();
            if(hasKey(getKey(userId))){
                //同步修改redis中的数据
                redis.opsForValue().set(getKey(userId),JSONUtil.toJsonPrettyStr(dbSettings));
            }
        }
        LogUtil.Operation.updateClinicSetting("{}修改设置：设置Id={}", LoginUser.get().getName(), settings.getId());
        transactionManager.commit(transaction);

        resetBusCache();
        return Result.success(true);
    }

    /**
     * 重置营业相关实例缓存
     */
    private void resetBusCache() {
            Opt<String> busOpt = redisUtil.getOpt(RedisKeys.USER_BUSINESS_BY_ID.key(LoginUser.getId()));
            if (!busOpt.isEmpty()){
                String busKey = RedisKeys.USER_BUSINESS_BY_ID.key(LoginUser.getId());//营业实例键
                redis.delete(busKey);//仅删除就行，再次获取时会自动查库再加入缓存
            }
    }

    @Override
    public Settings getByUserId() {
        Long userId = LoginUser.getId();
        //先查询redis中的数据
        String str = redis.opsForValue().get(getKey(userId));
        //不存在再从数据库中取
        if(Objects.isNull(str)){
            Settings one = lambdaQuery().eq(Settings::getUserId, userId).one();
            if(isNull(one)) return null;
            one.setBusinessDayList(JSON.parseArray(one.getBusinessDay(), String.class));
            one.setBusinessTimeList(getBusinessTime(one.getBusinessTime()));
            redis.opsForValue().set(getKey(userId), JSONUtil.toJsonPrettyStr(one));
            return one;
        } else {
            Settings cache = JSONUtil.toBean(str, Settings.class);
            cache.setBusinessDayList(JSON.parseArray(cache.getBusinessDay(), String.class));
            cache.setBusinessTimeList(getBusinessTime(cache.getBusinessTime()));
            return cache;
        }
    }

    @Override
    public Integer getUserSettingStockExpiryAlertMonth(Settings settings) {
        return nonNull(settings) && nonNull(settings.getExpiryAlertMonth()) ? settings.getExpiryAlertMonth() : stockDefaultExpiryAlertMonth;
    }

    @Override
    public Map<String, Object> selectAddr() {
        Settings one = lambdaQuery()
                .select(Settings::getProvinceId, Settings::getAddr)
                .eq(Settings::getUserId, LoginUser.getId())
                .one();
        return new HashMap<String, Object>(){{
            put("provinceId", one.getProvinceId());
            put("addr", one.getAddr());
        }};
    }

    @Override
    public Boolean updateAddr(BigInteger provinceId, String addr) {
        return lambdaUpdate()
                .set(Settings::getAddr, addr)
                .set(Settings::getProvinceId, provinceId)
                .eq(Settings::getUserId, LoginUser.getId())
                .update();
    }

    /**
     * 获取所有时间段的营业时间
     *
     * @param jsonArrStr 所有时间段的营业时间JSON字符串
     */
    private List<Map<Integer, List<String>>> getBusinessTime(String jsonArrStr) {

        //变量、注释没改，实际上value里的值变更为当天时分秒列表。

        if (StringUtils.isBlank(jsonArrStr) || (jsonArrStr.length() == NumberUtils.INTEGER_ONE && jsonArrStr.charAt(0) == 127))
            return Collections.emptyList();

        List<String> allTimeStrList = JSON.parseArray(jsonArrStr, String.class);//所有时间段JSON字符串
        List<Map<Integer, List<String>>> resultList = new ArrayList<>();//所有时间段列表
        for (String nowTimeMapStr : allTimeStrList) {
            Map tmpTimeStrMap = JSON.parseObject(nowTimeMapStr, Map.class);//当前时间段的起始、结束时间戳字符串列表
            if (tmpTimeStrMap.size() > NumberUtils.INTEGER_ONE)
                return Collections.emptyList();

            Set<Map.Entry> tmpSet = tmpTimeStrMap.entrySet();
            for (Map.Entry nowTimeMap : tmpSet) {
                Map<Integer, List<String>> dateMapByNow = new HashMap<>();//当前时间段的起始、结束日期映射
                List<String> dateListByNow = new ArrayList();//当前时间段的起始、结束日期列表

                //初始化当前时间段的起始、结束日期列表
                JSONArray nowTimeJsonArr = JSON.parseArray(String.valueOf(nowTimeMap.getValue()));
                if (nowTimeJsonArr.size() > NumberUtils.INTEGER_TWO)
                    return Collections.emptyList();
                dateListByNow.add(nowTimeJsonArr.getString(NumberUtils.INTEGER_ZERO));
                dateListByNow.add(nowTimeJsonArr.getString(NumberUtils.INTEGER_ONE));

                dateMapByNow.put(Integer.valueOf(String.valueOf(nowTimeMap.getKey())), dateListByNow);
                resultList.add(dateMapByNow);
            }
        }

        return resultList;
    }

    private Boolean hasKey(String key){
        return redis.hasKey(key);
    }


    private String getKey(Long uid) {
        return SETTING_KEY_PREFIX + uid;
    }
}




