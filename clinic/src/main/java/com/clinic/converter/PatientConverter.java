package com.clinic.converter;

import com.clinic.dto.param.AddPatientParam;
import com.clinic.dto.param.EditPatientParam;
import com.clinic.dto.param.PatientParam;
import com.clinic.entity.Patient;
import com.clinic.entity.RetailRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientConverter {
    Patient toEntity(AddPatientParam param);

    Patient toEntity(PatientParam param);

    Patient toEntity(EditPatientParam param);

    Patient toEntity(RetailRecord retailRecord);
}
