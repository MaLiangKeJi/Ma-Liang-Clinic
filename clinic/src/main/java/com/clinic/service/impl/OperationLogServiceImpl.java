package com.clinic.service.impl;

import com.clinic.entity.OperationLog;
import com.clinic.service.OperationLogService;
import com.clinic.mapper.OperationLogMapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【log_operation(操作日志)】的数据库操作Service实现
* @createDate 2023-12-30 18:34:19
*/
@Service
public class OperationLogServiceImpl extends MPJBaseServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{

}




