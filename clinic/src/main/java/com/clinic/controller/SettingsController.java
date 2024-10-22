package com.clinic.controller;

import com.bbs.Result;
import com.clinic.dto.param.AddSettingsParam;
import com.clinic.dto.param.UpdateSettingsParam;
import com.clinic.entity.Settings;
import com.clinic.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Map;


@Slf4j
@RestController
public class SettingsController {

    @Resource
    private SettingsService service;

    /**
     * 添加设置
     * @param param AddSettingsParam
     * @return 添加结果
     */
    @PutMapping("/settings")
    public Result<Boolean> add(@RequestBody @Valid AddSettingsParam param){
        return service.add(param);
    }


    /**
     * 修改设置
     * @param param UpdateSettingsParam
     * @return 修改结果
     */
    @PostMapping("/settings")
    public Result<Boolean> update(@RequestBody @Valid UpdateSettingsParam param){
        return service.update(param);
    }

    /**
     * 用户设置查询
     * @return Settings
     */
    @GetMapping("/settings")
    public Result<Settings> getByUserId(){
        return Result.success(service.getByUserId());
    }

    /**
     * 查询地址
     * @return
     */
    @GetMapping("/settings/addr")
    public Result<Map<String, Object>> getAddr(){
        return Result.success(service.selectAddr());
    }

    /**
     * 修改地址
     * @return
     */
    @PutMapping("/settings/addr")
    public Result<Boolean> updateAddr(BigInteger provinceId, String addr){
        return Result.success(service.updateAddr(provinceId,addr));
    }


}
