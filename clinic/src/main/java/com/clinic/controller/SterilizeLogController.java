package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.param.AddSterilizeLogParam;
import com.clinic.dto.param.SearchSterilizeLogParam;
import com.clinic.entity.SterilizeLog;
import com.clinic.service.SterilizeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 消毒
 */
@Slf4j
@RestController
@RequestMapping("/log/sterilize")
public class SterilizeLogController {

    @Resource
    private SterilizeLogService service;

    /**
     * 添加记录
     * @param param AddSterilizeLogParam
     * @return 添加结果
     */
    @PutMapping
    public Result<Boolean> add(@RequestBody @Valid AddSterilizeLogParam param){
        return service.add(param);
    }

    /**
     * 消毒记录查询
     * @param param SearchSterilizeLogParam
     * @return IPage<QuerySterilizeLogDto>
     */
    @GetMapping
    public Result<Page<SterilizeLog>> search(SearchSterilizeLogParam param){
        return service.search(param);
    }

}
