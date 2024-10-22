package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.param.AddDisinfectionLogParam;
import com.clinic.dto.param.SearchDisinfectionLogParam;
import com.clinic.entity.DisinfectionLog;
import com.clinic.service.DisinfectionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 消杀
 */
@Slf4j
@RestController
@RequestMapping("/log/disinfection")
public class DisinfectionLogController {

    @Resource
    private DisinfectionLogService service;

    /**
     * 添加记录
     * @param param AddDisinfectionLogParam
     * @return 添加结果
     */
    @PutMapping
    public Result<Boolean> add(@RequestBody @Valid AddDisinfectionLogParam param){
        return service.add(param);
    }

    /**
     * 查询记录
     * @param param SearchDisinfectionLogParam
     * @return IPage<QuerySterilizeLogDto>
     */
    @GetMapping
    public Result<Page<DisinfectionLog>> search(SearchDisinfectionLogParam param){
        return service.search(param);
    }
}
