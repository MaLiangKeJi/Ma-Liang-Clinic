package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.api.auth.User;
import com.bbs.vo.BaseParam;
import com.clinic.cache.log.admission.AdmissionLogCache;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.dto.param.SearchAdmissionParam;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.OperationLog;
import com.clinic.entity.Patient;
import com.clinic.service.AdmissionLogService;
import com.clinic.service.OperationLogService;
import com.clinic.service.PatientService;
import com.clinic.util.LoginUser;
import com.clinic.util.log.LogUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 日志
 */
@RestController
public class LogController {

    @Resource
    private AdmissionLogCache admissionLogCache;
    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private AdmissionLogService database;
    @Resource
    private OperationLogService operationLogService;
    @Resource
    private PatientService patientService;

    /**
     * 记录就诊日志
     * @param param 就诊信息
     * @return null
     */
    @PutMapping("/log/admission")
    public Result<Long> recordAdmission(@RequestBody RecordAdmissionLogParam param) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        User loginUser = LoginUser.get();
        try {
            if(isNull(param.getPatientId())) {
                Patient patient = param.getPatient();
                patient.setUserId(loginUser.getId());
                patientService.save(patient);
                param.setPatientId(patient.getId());
                LogUtil.Operation.addPatient(patient.getId(), "{}添加病人：病人id={}", loginUser.getName(), patient.getId());
            }
            Long logId = admissionLogCache.save(param);

            LogUtil.Operation.reception(param.getPatientId(), logId, "{}通过已有病人添加了门诊日志：病人id={}, 门诊日志id={}", LoginUser.get().getName(), param.getPatientId(), logId);
            transactionManager.commit(transaction);
            return Result.success(logId);
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            e.printStackTrace();
        }
        return Result.failedNull();
    }


    /**
     * 查询就诊日志
     * @param param SearchAdmissionParam
     */
    @GetMapping("/log/admission")
    public Result<Page<AdmissionLog>> searchAdmission(SearchAdmissionParam param) throws ParseException {
        return Result.success(database.search(param));
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SearchOperationLogParam extends BaseParam {

        private Long userId = LoginUser.getId();
    }

    @GetMapping("/log/operation")
    public Result<Page<OperationLog>> searchOperationLog(SearchOperationLogParam param) {
        return Result.success(operationLogService.lambdaQuery()
                .eq(nonNull(param.userId), OperationLog::getUserId, param.userId)
                .orderByDesc(OperationLog::getCreateTime)
                .page(param.toPage())
        );
    }
}
