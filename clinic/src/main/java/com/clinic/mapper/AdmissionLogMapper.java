package com.clinic.mapper;

import com.clinic.entity.AdmissionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 路晨霖
* @description 针对表【admission_log(接诊日志)】的数据库操作Mapper
* @createDate 2023-11-03 18:41:33
* @Entity com.clinic.entity.AdmissionLog
*/
@Mapper
public interface AdmissionLogMapper extends MPJBaseMapper<AdmissionLog> {

}




