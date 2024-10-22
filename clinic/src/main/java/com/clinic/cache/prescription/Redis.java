package com.clinic.cache.prescription;

import cn.hutool.json.JSONUtil;
import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.util.LoginUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.clinic.enums.RedisKeys.PRESCRIPTION;

@Component
public class Redis {

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    public void loadToCache(List<PrescriptionSearchDrugVO> vos) {
        redis.opsForValue().multiSet(vos.stream().collect(Collectors
                .toMap(
                        vo -> PRESCRIPTION.key(vo.getId()),
                        JSONUtil::toJsonPrettyStr)
        ));
    }
}
