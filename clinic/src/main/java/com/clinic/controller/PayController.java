package com.clinic.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.util.PageUtil;
import com.clinic.app.AppPayService;
import com.clinic.app.AppPrescriptionService;
import com.clinic.app.AppStockService;
import com.clinic.cache.pay.PayCache;
import com.clinic.converter.PayConverter;
import com.clinic.dto.GetPayDto;
import com.clinic.dto.PayAndRecordPageDto;
import com.clinic.dto.PayRecordPatientDto;
import com.clinic.dto.PrescriptionDto;
import com.clinic.dto.param.GetPayParam;
import com.clinic.dto.param.PatientPayRecordParam;
import com.clinic.dto.param.ReturnPayRecordParam;
import com.clinic.dto.param.UpdatePayById;
import com.clinic.entity.AdmissionLog;
import com.clinic.enums.PayStateEnum;
import com.clinic.enums.RedisKeys;
import com.clinic.service.AdmissionLogService;
import com.clinic.service.PayService;
import com.clinic.util.LoginUser;
import com.clinic.util.RedisUtil;
import com.clinic.util.WebSocketUtil;
import com.clinic.util.WxUtil;
import com.clinic.util.log.LogUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;


@Slf4j
@RestController
public class PayController {

    private final AdmissionLogService admissionLogService;
    private final AppPayService service;

    private final PayCache payCache;
    private final PayConverter payConverter;

    private final AppStockService appStockService;

    private final AppPrescriptionService appPrescriptionService;

    private final DataSourceTransactionManager transactionManager;

    private final TransactionDefinition transactionDefinition;
    @Resource
    private WxUtil wxUtil;
    @Resource
    private PayService payService;

    @Autowired
    private RedisUtil redis;

    @Autowired
    private WebSocketUtil webSocket;

    /**
     * 本次收费-数据回显
     */
    @GetMapping("/pay")
    public Result<GetPayDto> getPay(@NotNull Long id){
        return service.getPay(id);
    }

    /**
     * 病人收费记录
     */
    @GetMapping("/pay/patient")
    public Result<Page<PayRecordPatientDto>> getPayPatient(PatientPayRecordParam param) throws InterruptedException {
        return payCache.selectPayPatient(param);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllIsPayParam {

        private Integer current;
        private Integer size;
        private String val;
        private Long startDate;
        private  Long endDate;
    }

    /**
     * 收费列表-已收费
     */
    @GetMapping("/pay/all/is")
    public Result<Page<PayAndRecordPageDto>> getPay(AllIsPayParam param){
        if(isNull(param.getCurrent())) param.setCurrent(INTEGER_ONE);
        if(isNull(param.getSize())) param.setCurrent(10);
        GetPayParam getPayParam = new GetPayParam(new Page<>(param.getCurrent(), param.getSize()), param.getVal(), param.getStartDate(), param.getEndDate());
        getPayParam.setState(INTEGER_ONE);
        List<PayAndRecordPageDto> data = payService.selectPayAndRecordDto(getPayParam);
        return Result.success(PageUtil.paginateWithInfo(data, param.getCurrent(), param.getSize()));
    }

    /**
     * 收费列表-未收费
     */
    @GetMapping("/pay/all/no")
    public Result<Page<PayAndRecordPageDto>> getNoPay(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String val,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate
    ){
        GetPayParam param = new GetPayParam(new Page<>(current, size), val, startDate, endDate);
        param.setState(INTEGER_ZERO);
        List<PayAndRecordPageDto> data = payService.selectPayAndRecordDto(param);
        return Result.success(PageUtil.paginateWithInfo(data, current, size));
    }

    /**
     * 收费列表-退费
     */
    @GetMapping("/pay/all/return")
    public Result<Page<PayAndRecordPageDto>> getPay(ReturnPayRecordParam param){
        GetPayParam p = payConverter.toParam(param);
        return service.selectPayRecord(p);
    }


    /**
     * 本次收费-修改收费状态和收费方式
     */
    @PostMapping("/pay")
    public Result<Boolean> updatePayById(@RequestBody UpdatePayById param){
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            AdmissionLog admissionLog = admissionLogService.getById(param.getAdmissionId());
            //收费-扣库存
            param.setState(PayStateEnum.IS_PAY.getCode());
            //修改收费
            if(!service.updatePayById(param))throw new RuntimeException();
            //根据支付id,查出处方数据
            PrescriptionDto prescriptionDto = appPrescriptionService.getByPayId(param.getId());
            Long patientId = prescriptionDto.getPatientId();
            //根据处方数据，扣库存
            if(!appStockService.updateNum(prescriptionDto))throw new RuntimeException();
            //修改门诊日志状态
            if (!admissionLogService.updateEndState(param.getAdmissionId()))throw new RuntimeException();
            LogUtil.Operation.pay(patientId, param.getAdmissionId(), "{}就诊收费-修改收费状态和收费方式：收费id={}, 处方id={}", LoginUser.get().getName(), param.getId(), prescriptionDto.getId());
            // 发送微信消息
            if(StrUtil.isNotBlank(admissionLog.getOpenId())){
                wxUtil.sendPrescriptionMassage(admissionLog,prescriptionDto);
            }
            transactionManager.commit(transaction);

            updOpenDrug(admissionLog.getPrescriptionId());

            return Result.success(true);
        } catch (IOException e) {
            transactionManager.rollback(transaction);
            return Result.failed("给药房发送处方信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            transactionManager.rollback(transaction);
            return Result.failed();
        }
    }

    /**
     * 更新药房使用的处方
     *
     * @param presId 处方ID
     */
    private void updOpenDrug(Long presId) throws IOException {
        Opt<String> get = redis.hashGet(RedisKeys.DRUG_OPEN_BY_ID.key(LoginUser.getId()),
                presId.toString());
        if (!get.isEmpty()) {
            JSONObject jObj = JSONUtil.parseObj(get.get());
            jObj.set("payStatus", INTEGER_ONE);

            redis.hashSet(
                    RedisKeys.DRUG_OPEN_BY_ID.key(LoginUser.getId()),
                    presId.toString(),
                    jObj.toString());

            webSocket.sendMessageTo(jObj.toString(), LoginUser.getId());
        }
    }

    @Autowired
    public PayController(AdmissionLogService admissionLogService, AppPayService service, PayCache payCache, PayConverter payConverter, AppStockService appStockService, AppPrescriptionService appPrescriptionService, DataSourceTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        this.admissionLogService = admissionLogService;
        this.service = service;
        this.payCache = payCache;
        this.payConverter = payConverter;
        this.appStockService = appStockService;
        this.appPrescriptionService = appPrescriptionService;
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
    }
}
