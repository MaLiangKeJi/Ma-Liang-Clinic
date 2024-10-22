package com.clinic.converter;

import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.StockUnit;
import com.clinic.entity.Unit;
import com.clinic.dto.param.UnitParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UnitConverter {

    Unit toEntity(UnitParam param);

    @Mapping(source = "id", target = "unitId")
    StockUnit toEntity(PutStockParam.StockUnitParam param);
}
