package com.clinic.cache.set;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bbs.exception.BusinessException;
import com.clinic.controller.SettingsController;
import com.clinic.dto.BusinessVO;
import com.clinic.entity.Settings;
import com.clinic.enums.RedisKeys;
import com.clinic.util.LoginUser;
import com.clinic.util.RedisUtil;
import com.clinic.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 诊所设置缓存
 */
@Slf4j
@Component
public class BusinessCache {

    @Autowired
    private RedisUtil redis;

    @Resource
    private ApplicationContext appContext;

    private final class StringConstant {
        private static final String SHI_QU = "+8";

        private static final String CACHE_DEL_FAIL = "营业信息删除失败";
    }

    public BusinessVO get() {
        Opt<String> busOpt = redis.getOpt(RedisKeys.USER_BUSINESS_BY_ID.key(LoginUser.getId()));

        String busKey = RedisKeys.USER_BUSINESS_BY_ID.key(LoginUser.getId());//营业实例键
        if (busOpt.isEmpty()) {//查数据库
            return addCache(busKey);
        } else {//缓存处理
            String busJStr = redis.get(busKey);//营业实例JSON字符串
            BusinessVO vo = JSONUtil.toBean(busJStr, BusinessVO.class);
            if (isNowDay(vo.getDayTime()) && !vo.getBusinessDayList().isEmpty() && !vo.getBusinessTimeList().isEmpty())//今日的营业实例直接返回
                return vo;
            else {//昨日的营业实例再处理
                if (redis.delete(busKey))
                    return addCache(busKey);
                throw new BusinessException(StringConstant.CACHE_DEL_FAIL);
            }
        }
    }

    /**
     * 添加缓存
     *
     * @param busKey 营业实例键
     */
    private BusinessVO addCache(String busKey) {
        return addCacheCore(busKey, getBusVOByDB());
    }

    /**
     * 添加缓存
     *
     * @param busKey 营业实例键
     */
    private BusinessVO addCacheCore(String busKey, BusinessVO vo) {
        //事务
        redis.multiSet(new HashMap<String, String>() {{
            put(busKey, JSONUtil.toJsonStr(vo));
        }});

        expire(busKey);//过期时间

        return vo;
    }

    private static String DEFAULT_BUSINESS_DAY = "[1,2,3,4,5]";

    private static String DEFAULT_BUSINESS_TIME = "[{1:[080000,120000]},{2:[140000,180000]}]";

    /**
     * 从数据库获取营业实例
     */
    private BusinessVO getBusVOByDB() {
        Settings set = SpringUtil.getRespData(SettingsController.class, appContext, SettingsController::getByUserId);
        String dayJsonStr = set.getBusinessDay();//营业天数JSON字符串
        String timeJsonStr = set.getBusinessTime();//所有时间段的营业时间JSON字符串

        BusinessVO vo = new BusinessVO();
        vo.setPhysician(set.getPhysician());
        vo.setDayTime(DateTime.now().getTime());
        vo.setBusinessDayList(JSONUtil.toList(StringUtils.isNotBlank(dayJsonStr) ? dayJsonStr : DEFAULT_BUSINESS_DAY, String.class));
        vo.setBusinessTimeList(getTimesList(StringUtils.isNotBlank(timeJsonStr) ? timeJsonStr : DEFAULT_BUSINESS_TIME));

        return vo;
    }

    /**
     * 获取所有时间段的营业时间的时间戳列表
     *
     * @param timeListStr 所有时间段的营业时间JSON字符串
     */
    private List<List<String>> getTimesList(String timeListStr) {
        List<List<String>> resultList = new ArrayList<>();

        JSONArray timeMapListByJson = JSONUtil.parseArray(timeListStr);//所有时间段映射的营业时间列表
        for (int i = 0; i < timeMapListByJson.size(); i++) {
            //获取当前时间段的起始、结束时间戳列表
            JSONObject timeMapByJObj = (JSONObject) timeMapListByJson.get(i);
            Collection<Object> timeListByNowOfOri = timeMapByJObj.getRaw().values();
            if (timeListByNowOfOri.size() > NumberUtils.INTEGER_ONE)
                return Collections.emptyList();

            //初始化返回列表
            List<String> timeListByNow = new ArrayList<>();
            timeListByNowOfOri.forEach(l -> {
                JSONArray timeJArr = (JSONArray) l;
                timeJArr.forEach(t -> timeListByNow.add(t.toString()));
            });
            resultList.add(timeListByNow);
        }

        return resultList;
    }

    /**
     * @param busKey 营业实例键
     */
    public void expire(String busKey) {
        redis.expire(busKey, 1, TimeUnit.DAYS);
    }

    /**
     * 是否是今日
     *
     * @param cacheTime 缓存的时间戳
     */
    private boolean isNowDay(Long cacheTime) {
        Long timeByDayOfStart = LocalDate.now().atStartOfDay()//当日起始时间戳
                .toInstant(ZoneOffset.of(StringConstant.SHI_QU)).toEpochMilli();
        Long timeByDayOfEnd = LocalDate.now().plusDays(NumberUtils.INTEGER_ONE).atStartOfDay()
                .plusSeconds(NumberUtils.INTEGER_MINUS_ONE)//当日结束时间戳
                .toInstant(ZoneOffset.of(StringConstant.SHI_QU)).toEpochMilli();

        return (timeByDayOfStart <= cacheTime) && (timeByDayOfEnd >= timeByDayOfEnd);
    }

    /**
     * 切换营业状态并返回营业实例
     *
     * @param workFlag 工作标识符:0:营业;1:不营业;
     */
    public BusinessVO reverseWork(Integer workFlag) {
        //重置标识符
        BusinessVO vo = get();
        vo.setIsWork(workFlag.equals(NumberUtils.INTEGER_ZERO) ? true : false);

        //重置缓存
        String busKey = RedisKeys.USER_BUSINESS_BY_ID.key(LoginUser.getId());//营业实例键
        if (redis.delete(busKey))
            return addCacheCore(busKey, vo);
        throw new BusinessException(StringConstant.CACHE_DEL_FAIL);
    }
}