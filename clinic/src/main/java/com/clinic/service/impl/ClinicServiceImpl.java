package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.Clinic;
import com.clinic.service.ClinicService;
import com.clinic.mapper.ClinicMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* @author 路晨霖
* @description 针对表【clinic(诊所信息)】的数据库操作Service实现
* @createDate 2023-09-24 10:42:27
*/
@Service
public class ClinicServiceImpl extends ServiceImpl<ClinicMapper, Clinic>
    implements ClinicService{

    private final Map<Long, Clinic> cache = new HashMap<>();

    @Override
    public Clinic searchByUserId(Long uid) {
        Clinic clinic = cache.get(uid);
        if(Objects.isNull(clinic)) {
            clinic = lambdaQuery().eq(Clinic::getUserId, uid).one();
            if(Objects.isNull(clinic)) return null;
            cache.put(uid, clinic);
        }
        return clinic;
    }
}




