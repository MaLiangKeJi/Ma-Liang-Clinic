package com.clinic.converter;

import com.clinic.dto.DrugDto;
import com.clinic.dto.DrugLotVo;
import com.clinic.dto.param.DrugDetailParam;
import com.clinic.dto.param.DrugLotParam;
import com.clinic.entity.Drug;
import com.clinic.entity.DrugDetail;
import com.clinic.entity.DrugLot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DrugLotConverter {
    DrugLot paramToEntity(DrugLotParam param);

    List<DrugLotVo> toDto(List<DrugDetail> drugBatchDetails);

    DrugLotVo drugDetailToDrugDto(DrugDetail drugDetail);

    List<DrugDetail> toDetailEntity(List<DrugDetailParam> drugs);

    List<DrugDto> detailEntityToDrug(List<Drug> selectInfoDistinct);
}
