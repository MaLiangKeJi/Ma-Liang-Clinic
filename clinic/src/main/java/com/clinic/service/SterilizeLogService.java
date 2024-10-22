package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;
import com.clinic.dto.param.AddSterilizeLogParam;
import com.clinic.dto.param.SearchSterilizeLogParam;
import com.clinic.entity.SterilizeLog;

/**
 * 消毒记录
 */
public interface SterilizeLogService extends IService<SterilizeLog> {

    Result<Boolean> add(AddSterilizeLogParam param);

    Result<Page<SterilizeLog>> search(SearchSterilizeLogParam param);
}
