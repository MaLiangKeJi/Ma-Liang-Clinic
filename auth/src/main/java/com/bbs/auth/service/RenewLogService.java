package com.bbs.auth.service;

import com.bbs.auth.entity.RenewLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface RenewLogService extends IService<RenewLog> {

    RenewLog getByOrderId(String outTradeNo);

    List<RenewLog> getListByUserId(Long id);
}
