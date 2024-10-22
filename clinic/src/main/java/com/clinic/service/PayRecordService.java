package com.clinic.service;

import com.clinic.entity.PayRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 其他收费
 */
public interface PayRecordService extends IService<PayRecord> {

    /**
     * 查询支付 ID 对应的其他收费条目（不包含 处方 ！）
     * @param payId 支付 ID
     * @return 其他收费条目
     */
    List<PayRecord> searchByPayId(Long payId);

    PayRecord getByPayId(Long payId);
}
