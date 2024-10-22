package com.clinic.converter;

import com.clinic.dto.param.AddDisinfectionLogParam;
import com.clinic.dto.param.AddSterilizeLogParam;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.DisinfectionLog;
import com.clinic.entity.SterilizeLog;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Mapper(componentModel = "spring")
@Repository
public interface LogConverter {

    AdmissionLog toAdmissionEntity(RecordAdmissionLogParam param);

    SterilizeLog toSterilizeEntity(AddSterilizeLogParam param);

    DisinfectionLog toDisinfectionEntity(AddDisinfectionLogParam param);
}
