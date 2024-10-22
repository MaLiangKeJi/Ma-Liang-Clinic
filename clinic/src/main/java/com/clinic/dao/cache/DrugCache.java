package com.clinic.dao.cache;


import com.clinic.converter.DrugLotConverter;
import com.clinic.dto.DrugDto;
import com.clinic.service.DrugService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DrugCache implements InitializingBean {

    private DrugService drugService;

    private DrugLotConverter converter;

    private List<DrugDto> drugList;

    public List<DrugDto> getDrugList() {
        return drugList;
    }

    @Resource
    public void setConverter(DrugLotConverter converter) {
        this.converter = converter;
    }

    @Resource
    public void setMapper(DrugService mapper) {
        this.drugService = mapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        drugList = converter.detailEntityToDrug(drugService.selectInfoDistinct());
    }
}
