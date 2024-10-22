package com.clinic.cache.inform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.clinic.entity.Inform;
import com.clinic.enums.RedisKeys;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InformCache {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    /**
     * 获取所有消息
     * @param userId 用户ID
     * @return List<Inform>
     */
    public Map<String,Object> getInformList(Long userId) {
        List<Inform> result = new ArrayList<>();
        List<String> notReadInformIds = redis.opsForList().range(informKey(), NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_MINUS_ONE);
        int userIsReadInformId = NumberUtils.INTEGER_MINUS_ONE;
        if(ObjUtil.isNotEmpty(userId)){
            //获取指定用户的未读消息
            String userIsReadInformIdStr = redis.opsForValue().get(informUserKey(userId));
            //没有数据说明是新用户，需修改为全部未读
            if(StrUtil.isEmpty(userIsReadInformIdStr)){
                redis.opsForValue().set(informUserKey(userId), String.valueOf(NumberUtils.INTEGER_ZERO));
            }else{
                userIsReadInformId = Integer.parseInt(userIsReadInformIdStr);
            }
        }
        boolean hasNoRead = false;
        if(CollUtil.isNotEmpty(notReadInformIds)){
            for (int i = 0; i < notReadInformIds.size(); i++) {
                Inform bean = JSONUtil.toBean(notReadInformIds.get(i), Inform.class);
                if(userIsReadInformId == NumberUtils.INTEGER_MINUS_ONE || i > (userIsReadInformId+1)){
                    bean.setIsNotRead(true);
                    hasNoRead = true;
                }
                result.add(bean);
            }
        }
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", result);
        hashMap.put("hasNoRead", hasNoRead);
        return hashMap;
    }



    /**
     * 添加消息
     * @param inform 消息
     */
    public void putInform(Inform inform) {
        redis.opsForList().rightPush(informKey(), JSONUtil.toJsonStr(inform));
    }

    /**
     * 更新用户已读消息
     * @param userId 用户ID
     */
    public void updateUserReadInform(Long userId) {
        Long size = redis.opsForList().size(informKey());
        redis.opsForValue().set(informUserKey(userId), String.valueOf(size));
    }


    public String informUserKey(Long userId) {
        return RedisKeys.INFORM_USER.key(userId);
    }

    public String informKey() {
        return RedisKeys.INFORM.getPrefix();
    }


}
