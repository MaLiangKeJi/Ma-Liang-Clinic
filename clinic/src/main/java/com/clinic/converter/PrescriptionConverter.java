package com.clinic.converter;

import com.clinic.app.porescription.file.create.CreatePrescriptionFile;
import com.clinic.app.reception.over.PrescriptionParam;
import com.clinic.dto.PrescriptionDrugDto;
import com.clinic.dto.param.SaveOrUpdatePrescription;
import com.clinic.dto.param.SaveOrUpdatePrescriptionDrug;
import com.clinic.dto.param.UpdatePrescription;
import com.clinic.entity.Prescription;
import com.clinic.entity.PrescriptionDrug;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionConverter {

    Prescription toEntity(PrescriptionParam param);

    Prescription toEntity(UpdatePrescription param);

    List<PrescriptionDrug> toEntityDrug(List<SaveOrUpdatePrescriptionDrug> drugList);

    List<PrescriptionDrugDto> toDto(List<PrescriptionDrug> list);

    Prescription toEntity(SaveOrUpdatePrescription param);

    CreatePrescriptionFile.Drug toFileModel(PrescriptionDrugDto dto);
}
