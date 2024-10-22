package com.clinic.converter;

import com.clinic.dto.QueryStockInDrugDto;
import com.clinic.dto.QueryStockInDto;
import com.clinic.dto.param.PutStockParam;
import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockConverter {

    Stock toEntity(PutStockParam param);

    @Mapping(target = "singleDoseUnit", ignore = true)
    @Mapping(source = "type", target = "dosageForm")
    @Mapping(source = "countNumber", target = "countVal")
    @Mapping(source = "countUnitId", target = "countUnitId")
    @Mapping(source = "usage", target = "drugUsage")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "stateCountRule", ignore = true)
    StockBatch toBatchEntity(PutStockParam param);

    @Mapping(source = "type", target = "dosageForm")
    StockInDrug toStockInDrugEntity(PutStockParam param);

    @Mapping(target = "expiryDate", ignore = true)
    @Mapping(source = "number", target = "stockNumber")
    @Mapping(source = "number", target = "stockNumberUnit")
    @Mapping(target = "singleDoseUnit", ignore = true)
    @Mapping(target = "stateCountRule", ignore = true)
    PrescriptionSearchDrugVO toPrescriptionSearchDrugVO(StockBatch stockBatch);

    List<PrescriptionSearchDrugVO> toPrescriptionSearchDrugVOList(List<Drug> records);

    @Mapping(source = "type", target = "dosageForm")
    PrescriptionSearchDrugVO drugToPrescriptionSearchDrugVO(Drug drug);

    @Mapping(source = "stockInDrugs", target = "stockInDrugs", qualifiedByName = "getQueryStockInDrug")
    QueryStockInDto getQueryStockInDto(StockIn stockIn);

    @Named("getQueryStockInDrug")
    default QueryStockInDrugDto getQueryStockInDrug(StockInDrug drug) {
        return getQueryStockInDrugDto(drug);
    }

    QueryStockInDrugDto getQueryStockInDrugDto(StockInDrug drug);
}
