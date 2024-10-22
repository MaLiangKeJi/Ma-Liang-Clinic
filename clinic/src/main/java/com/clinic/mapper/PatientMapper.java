package com.clinic.mapper;

import com.clinic.entity.Patient;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 29080
* @description 针对表【patient(病人信息登记表)】的数据库操作Mapper
* @createDate 2023-05-24 17:28:35
* @Entity com.clinic.entity.Patient
*/
@Mapper
public interface PatientMapper extends MPJBaseMapper<Patient> {
}




