package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.RenewLog;
import com.bbs.auth.mapper.RenewLogMapper;
import com.bbs.auth.service.RenewLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class RenewLogServiceImpl extends ServiceImpl<RenewLogMapper, RenewLog>
    implements RenewLogService{

    @Override
    public RenewLog getByOrderId(String outTradeNo) {
        return lambdaQuery().eq(RenewLog::getOrderId, outTradeNo).one();
    }

    @Override
    public List<RenewLog> getListByUserId(Long id) {
        return lambdaQuery().eq(RenewLog::getCreateBy, id).eq(RenewLog::getDeleteFlag, 0).list();
    }
}




