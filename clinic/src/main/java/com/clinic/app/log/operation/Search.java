package com.clinic.app.log.operation;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.entity.OperationLog;
import com.clinic.entity.Patient;
import com.clinic.service.OperationLogService;
import com.clinic.service.PatientService;
import com.clinic.util.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateUtil.*;
import static com.clinic.util.log.ServiceLogEnums.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController("searchOperationLog")
@RequestMapping
public class Search {

    @Resource
    private OperationLogService operationLogService;
    @Resource
    private PatientService patientService;

    @GetMapping("/log/operation/list")
    public Result<List<OperationLog>> search(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer current,
            @RequestParam(required = false) Integer size
    ) {
        if(isNull(current) && isNull(size)) {
            current = INTEGER_ONE;
            size = 10;
        }
        List<OperationLog> logs = operationLogService.lambdaQuery()
                .eq(OperationLog::getUserId, LoginUser.getId())
                .and(ObjUtil.isNotEmpty(patientId),o->o.eq(OperationLog::getPatientId, patientId).in(OperationLog::getServiceCode, Collections.singletonList(ADMISSION.getServiceCode())))
                // 只筛选部分，对诊所医生有用的操作日志类型
                .in(ObjUtil.isEmpty(patientId),OperationLog::getServiceCode, Arrays.asList(
                        STOCK_ADD.getServiceCode(),
                        RETAIL.getServiceCode(),
                        ADMISSION.getServiceCode(),
                        PAY.getServiceCode(),
                        PRESCRIPTION_ADD.getServiceCode(),
                        DIAGNOSIS_PROOF_ADD.getServiceCode(),
                        DISINFECTION_ADD.getServiceCode(),
                        STERILIZE_ADD.getServiceCode(),
                        DOSSIER_ADD.getServiceCode()
                )).page(new Page<>(current, size)).getRecords();

        fillPatient(logs);
        return Result.success(logs);
    }

    private void fillPatient(List<OperationLog> logs) {
        if(logs.size() > INTEGER_ZERO) {
            // 填充病人信息
            Date now = new Date();
            List<OperationLog> needFillPatientLogs = new ArrayList<>();
            Set<Long> needFillPatientLogIds = logs.stream().peek(log -> {
                Date createTime = log.getCreateTime();
                log.setIsCurrentDay(isSameDay(now, createTime));
                log.setIsCurrentYear(year(createTime) == thisYear());
                log.setCreateMD(format(createTime, "MM/dd"));
                log.setCreateYMD(formatDate(createTime));
                log.setCreateHMS(formatTime(createTime));
                log.setCreateHMSTwelveHourlySystem(format(createTime, "hh:mm:ss"));
            }).filter(log -> {
                Integer serviceCode = log.getServiceCode();
                // 零售药品
                return (
                        RETAIL.getServiceCode().equals(serviceCode) ||                      // 零售药品
                                ADMISSION.getServiceCode().equals(serviceCode) ||           // 接诊
                                PAY.getServiceCode().equals(serviceCode) ||                 // 支付
                                PRESCRIPTION_ADD.getServiceCode().equals(serviceCode) ||    // 开具处方
                                DIAGNOSIS_PROOF_ADD.getServiceCode().equals(serviceCode) || // 诊断证明
                                DOSSIER_ADD.getServiceCode().equals(serviceCode)            // 新增病例
                );
            }).peek(needFillPatientLogs::add).map(OperationLog::getPatientId).collect(Collectors.toSet());

            if(needFillPatientLogIds.size() > INTEGER_ZERO) {
                Map<Long, Patient> patientIdMap = patientService.listByIds(needFillPatientLogIds)
                        .stream().collect(Collectors.toMap(Patient::getId, patient -> patient));
                if(CollectionUtils.isNotEmpty(needFillPatientLogs)) {
                    needFillPatientLogs.forEach(log -> {
                        Patient patient = patientIdMap.get(log.getPatientId());
                        if(nonNull(patient)) {
                            if(nonNull(patient.getSex())) patient.setSexStr(Objects.equals(patient.getSex(), INTEGER_ONE) ? "男" : "女");
                            if(nonNull(patient.getAge())) patient.setAgeStr(patient.getAge() + "岁");
                            log.setPatient(patientIdMap.get(log.getPatientId()));
                        }
                    });
                }
            }
        }
    }
}
