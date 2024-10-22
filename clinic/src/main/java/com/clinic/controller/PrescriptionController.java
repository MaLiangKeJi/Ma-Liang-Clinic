package com.clinic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bbs.Result;
import com.clinic.app.AppPrescriptionService;
import com.clinic.app.porescription.file.create.CreatePrescriptionFile;
import com.clinic.cache.unit.UnitCache;
import com.clinic.cache.usage.UsageCache;
import com.clinic.dto.PrescriptionDto;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Unit;
import com.clinic.entity.Usage;
import com.clinic.service.AdmissionLogService;
import com.clinic.util.LoginUser;
import com.clinic.util.log.LogUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.hutool.core.util.ObjectUtil.isNotNull;


/**
 * 处方
 */
@Slf4j
@RestController
public class PrescriptionController {


    @Resource
    private AppPrescriptionService service;
    @Resource
    private AdmissionLogService admissionLogService;
    @Resource
    private UsageCache usageCache;
    @Resource
    private UnitCache unitCache;


    /**
     * 处方查询
     * @param dossierId 病人ID
     * @param patientId 病例ID
     * @param current 页码
     * @param size 条数
     * @return null
     */
    @GetMapping("/prescription")
    public Result<IPage<PrescriptionDto>> selectOr(Long dossierId, Long patientId, Integer current, Integer size){
        return service.selectOr(dossierId, patientId, current, size);
    }

    @Resource
    private CreatePrescriptionFile createPrescriptionFile;

    /**
     * 处方下载
     * @param admissionLogId 接诊记录ID
     * @param templateIndex 处方模板下标 API:/prescription/template/name/list
     * @see PrescriptionController
     * @throws Exception 下载异常
     */
    @GetMapping("/prescription/file")
    public Result<Boolean> getFile(@NotNull Long admissionLogId, @NotNull Integer templateIndex) throws Exception {
        AdmissionLog admissionLog = admissionLogService.getById(admissionLogId);
        Preconditions.checkArgument(isNotNull(admissionLog), "对应接诊记录不存在");
        Long prescriptionId = admissionLog.getPrescriptionId();
        Preconditions.checkArgument(isNotNull(prescriptionId), "未开具处方！");
        LogUtil.Operation.downloadPrescription(prescriptionId, "用户 {} 下载处方：处方id={}, 模板id={}", LoginUser.get().getName(), prescriptionId, templateIndex);
        createPrescriptionFile.generation(prescriptionId, templateIndex);
        return Result.success();
    }

    /**
     * 获取模板名称列表
     * @return 模板名称列表
     */
    @GetMapping("/prescription/template/name/list")
    public Result<List<String>> getTemplates() {
        return Result.success(createPrescriptionFile.getTemplates());
    }

    @GetMapping("/prescription/usage/list")
    public Result<List<Usage>> searchUsage(String name) {
        return Result.success(usageCache.search(name));
    }

    @GetMapping("/prescription/unit/list")
    public Result<List<Unit>> searchUnit(String name) {
        return Result.success(unitCache.search(name));
    }
}
