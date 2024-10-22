package com.clinic.mapper;

import com.clinic.entity.Prescription;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.clinic.entity.Prescription
 */
@Mapper
public interface PrescriptionMapper extends MPJBaseMapper<Prescription> {
//    PrescriptionFileVo selectJoinById(@Param("id") Long id);
}




