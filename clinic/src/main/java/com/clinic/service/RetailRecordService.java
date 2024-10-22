package com.clinic.service;

import com.clinic.entity.RetailRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 路晨霖
* @description 针对表【retail_record(零售：消费记录)】的数据库操作Service
* @createDate 2023-11-21 02:29:30
*/
public interface RetailRecordService extends IService<RetailRecord> {

    List<RetailRecord> list(String val, Long startDateLong, Long endDateLong);
}
