package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.RetailDrugRecord;
import com.clinic.service.RetailDrugRecordService;
import com.clinic.mapper.RetailDrugRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【retail_drug_record(零售：关联药品记录)】的数据库操作Service实现
* @createDate 2023-11-21 02:29:24
*/
@Service
public class RetailDrugRecordServiceImpl extends ServiceImpl<RetailDrugRecordMapper, RetailDrugRecord>
    implements RetailDrugRecordService{

}




