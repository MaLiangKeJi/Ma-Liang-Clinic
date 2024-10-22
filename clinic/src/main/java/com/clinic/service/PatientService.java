package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.AddOrEditPatientVo;
import com.clinic.dto.param.AddPatientParam;
import com.clinic.dto.param.EditPatientParam;
import com.clinic.dto.param.PatientParam;
import com.clinic.entity.Patient;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

/**
* @author 29080
* @description 针对表【patient(病人信息登记表)】的数据库操作Service
* @createDate 2023-05-24 17:28:35
*/
public interface PatientService extends MPJBaseService<Patient> {
    Result<AddOrEditPatientVo> add(AddPatientParam param);

    Result<Long> edit(EditPatientParam patient);

    Result<Page<Patient>> select(PatientParam param);

    List<Patient> select(String val);

    List<Patient> selectListByPhone(String phone);

    Patient selectByPhone(String phone);

    List<Patient> selectByName(String name);

    List<Patient> selectByName(String name, Long userId);
}
