package com.clinic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.param.DrugDetailParam;
import com.clinic.entity.DrugDetail;

import java.util.List;

/**
 *
 */
public interface DrugDetailService extends IService<DrugDetail> {

    List<DrugDetail> selectByDrug(DrugDetailParam param);

    List<DrugDetail> selectInfoByLot();

//    List<DrugDetail> selectInfoDistinct();

    List<DrugDetail> search(String val);
}
