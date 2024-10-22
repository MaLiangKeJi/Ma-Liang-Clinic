package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.PayRecord;
import com.clinic.mapper.PayRecordMapper;
import com.clinic.service.PayRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 其他收费
 */
@Service
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {

    @Override
    public List<PayRecord> searchByPayId(Long payId) {
        return lambdaQuery().eq(PayRecord::getPayId, payId).ne(PayRecord::getName, "处方").list();
    }

    @Override
    public PayRecord getByPayId(Long payId) {
        return lambdaQuery().eq(PayRecord::getPayId, payId).eq(PayRecord::getName, "处方").one();
    }
}




