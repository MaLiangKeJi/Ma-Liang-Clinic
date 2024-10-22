package com.clinic.converter;

import com.alibaba.fastjson.JSON;
import com.clinic.dto.param.AddSettingsParam;
import com.clinic.dto.param.UpdateSettingsParam;
import com.clinic.entity.Settings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface SettingsConverter {
    Settings toEntity(AddSettingsParam param);

    @Mapping(source = "businessDayList", target = "businessDay", qualifiedByName = "setBusinessDay")
    @Mapping(source = "businessTimes", target = "businessTime", qualifiedByName = "setBusinessTime")
    Settings toEntity(UpdateSettingsParam param);

    @Named("setBusinessDay")
    default String setBusinessDayStr(List<Integer> busList) {
        return JSON.toJSONString(busList);
    }

    @Named("setBusinessTime")
    default String setBusinessTimeStr(List<Map<Integer, List<String>>> busTimeList) {
        return JSON.toJSONString(busTimeList);
    }
}
