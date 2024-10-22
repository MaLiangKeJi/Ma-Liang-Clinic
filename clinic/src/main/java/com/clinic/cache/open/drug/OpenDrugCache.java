package com.clinic.cache.open.drug;

import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bbs.Result;
import com.clinic.dto.OpenDrugVO;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.enums.RedisKeys;
import com.clinic.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 药房开药缓存
 */
@Slf4j
@Component
public class OpenDrugCache {

    @Autowired
    private RedisUtil redis;

    public Result<List<OpenDrugVO>> get(Long uid) {
        Opt<Map<String, String>> busOpt = redis.hashGet(RedisKeys.DRUG_OPEN_BY_ID.key(uid));

        if (busOpt.isEmpty()) {//无数据返回空
            return Result.success(Collections.emptyList());
        } else {//缓存处理
            List<OpenDrugVO> drugList = new ArrayList<>();
            busOpt.get().forEach((k, v) -> {
                JSONObject jObj = JSONUtil.parseObj(v);
                String name = jObj.getStr("name");
                List<PrescriptionDrug> drugs = jObj.getBeanList("drugs", PrescriptionDrug.class);

                OpenDrugVO vo = new OpenDrugVO();
                vo.setName(name);
                vo.setDrugs(drugs);

                vo.setSexStatus(jObj.getInt("sexStatus"));
                vo.setPhone(jObj.getLong("phone"));
                vo.setFee(jObj.getBigDecimal("fee"));

                vo.setAdmissId(jObj.getLong("admissId"));
                vo.setPayId(jObj.getLong("payId"));
                vo.setPayStatus(jObj.getInt("payStatus"));
                vo.setTime(jObj.getLong("time"));
                drugList.add(vo);
            });
            return Result.success(drugList);
        }
    }
}