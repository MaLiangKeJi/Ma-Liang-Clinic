package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;
import com.clinic.dto.param.AddDisinfectionLogParam;
import com.clinic.dto.param.SearchDisinfectionLogParam;
import com.clinic.entity.DisinfectionLog;

/**
 * 消杀记录
 */
public interface DisinfectionLogService extends IService<DisinfectionLog> {

    Result<Boolean> add(AddDisinfectionLogParam param);

    Result<Page<DisinfectionLog>> search(SearchDisinfectionLogParam param);
}
