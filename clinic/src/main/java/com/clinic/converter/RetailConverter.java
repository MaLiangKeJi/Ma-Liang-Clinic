package com.clinic.converter;

import com.clinic.dto.param.AddRetailParams;
import com.clinic.entity.RetailDrugRecord;
import com.clinic.entity.RetailRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RetailConverter {

    RetailRecord toEntity(AddRetailParams params);

    List<RetailDrugRecord> toEntity(List<AddRetailParams.Drug> drugList);
}
