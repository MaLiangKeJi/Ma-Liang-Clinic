package com.clinic.service;

import com.clinic.entity.Clinic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 路晨霖
* @description 针对表【clinic(诊所信息)】的数据库操作Service
* @createDate 2023-09-24 10:42:27
*/
public interface ClinicService extends IService<Clinic> {

    Clinic searchByUserId(Long uid);
}
