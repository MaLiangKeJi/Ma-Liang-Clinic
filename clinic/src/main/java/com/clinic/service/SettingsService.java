package com.clinic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;
import com.clinic.dto.param.AddSettingsParam;
import com.clinic.dto.param.UpdateSettingsParam;
import com.clinic.entity.Settings;

import java.math.BigInteger;
import java.util.Map;

/**
 *
 */
public interface SettingsService extends IService<Settings> {


    Result<Boolean> add(AddSettingsParam param);

    Result<Boolean> update(UpdateSettingsParam param);

    Settings getByUserId();

    Integer getUserSettingStockExpiryAlertMonth(Settings settings);

    Map<String, Object> selectAddr();

    Boolean updateAddr(BigInteger provinceId, String addr);
}
