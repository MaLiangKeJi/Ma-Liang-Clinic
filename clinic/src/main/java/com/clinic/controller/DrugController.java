package com.clinic.controller;


import com.bbs.Result;
import com.clinic.cache.open.drug.OpenDrugCache;
import com.clinic.dto.Manufacturer;
import com.clinic.dto.OpenDrugVO;
import com.clinic.dto.param.DrugParam;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.service.AppDrugService;
import com.clinic.service.PrescriptionDrugService;
import com.clinic.util.SpringUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class DrugController {

    private AppDrugService appDrugService;

    @Resource
    private PrescriptionDrugService drugService;

    @Resource
    private ApplicationContext appContext;

    @GetMapping("/drug")
    public Result search(DrugParam param) {
        return appDrugService.search(param);
    }

    @GetMapping("/manufacturer")
    public Result searchManufacturer(Manufacturer param) {
        return appDrugService.searchManufacturer(param);
    }

    /**
     * 批量入库药品信息
     */
    @PostMapping("/import/excel")
    public Result excelImport(@RequestParam("file") MultipartFile file){
        return appDrugService.excelImport(file);
    }

    /**
     * 获取处方药列表
     *
     * @param presId 处方id
     */
    @GetMapping("/drug/wx/{presId}")
    public Result<List<PrescriptionDrug>> getDrug(@PathVariable Long presId) {
        List<PrescriptionDrug> drugList = drugService.selectJoinList(PrescriptionDrug.class,
                new MPJLambdaWrapper<PrescriptionDrug>()
                        .in(PrescriptionDrug::getPrescriptionId, presId));

        if (ObjectUtils.isEmpty(drugList))
            drugList = Collections.emptyList();

        return Result.success(drugList);
    }

    @GetMapping("/drug/unit/list/all")
    public Result getAllUnitList() {
        return appDrugService.searchAllDefUnit();
    }

    @GetMapping("/drug/unit/list")
    public Result getAllNoRepeatUnitList() {
        return appDrugService.searchNoRepeatAllDefUnit();
    }

    @GetMapping("/drug/unit/trace")
    public Result getUnitTrace() {
        return appDrugService.getDefUnitTrace();
    }

    /**
     * 查询给药房的处方药列表
     */
    @GetMapping("/drug/open")
    public Result<List<OpenDrugVO>> searchDrug(@RequestParam Long uid) {
        return SpringUtil.getResp(OpenDrugCache.class, appContext, d -> d.get(uid));
    }

    @Autowired
    public void setAppDrugService(AppDrugService appDrugService) {
        this.appDrugService = appDrugService;
    }
}
