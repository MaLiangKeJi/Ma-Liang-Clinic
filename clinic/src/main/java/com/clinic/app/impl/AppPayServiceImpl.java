package com.clinic.app.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.app.AppPayService;
import com.clinic.converter.PayConverter;
import com.clinic.dto.GetPayDto;
import com.clinic.dto.PayAndRecordPageDto;
import com.clinic.dto.PayRecordPatientDto;
import com.clinic.dto.param.GetPayParam;
import com.clinic.dto.param.PatientPayRecordParam;
import com.clinic.dto.param.UpdatePayById;
import com.clinic.entity.Dossier;
import com.clinic.entity.Pay;
import com.clinic.entity.PayRecord;
import com.clinic.entity.Prescription;
import com.clinic.service.PayRecordService;
import com.clinic.service.PayService;
import com.clinic.util.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
public class AppPayServiceImpl implements AppPayService {


    private PayService payService;

    private PayRecordService payRecordService;

    private PayConverter payConverter;


    /**
     * 本次收费-数据回显
     */
    @Override
    public Result<GetPayDto> getPay(Long id) {
        GetPayDto one = payService.getPay(id);
        return Result.success(one);
    }

    /**
     * 病人收费记录
     */
    @Override
    public Result<Page<PayRecordPatientDto>> selectPayPatient(PatientPayRecordParam param) {
        LambdaQueryChainWrapper<Pay> lambdaQuery = payService.lambdaQuery();
        lambdaQuery.eq(Objects.nonNull(param.getPatientId()), Pay::getPatientId,param.getPatientId())
                .eq(Pay::getCreator, LoginUser.getId())
                .orderByAsc(Pay::getState);
        Page<Pay> page = lambdaQuery.page(param.toPage());
        Page<PayRecordPatientDto> result = payConverter.ToDtoPage(page);
        return Result.success(result);
    }

    /**
     * 收费列表
     */
    @Override
    public Result<Page<PayAndRecordPageDto>> selectPayRecord(GetPayParam param) {
        Page<PayAndRecordPageDto> result = payService.selectPayAndRecordPageDto(param);
        return Result.success(result);
    }




    private void removePayOther(Long payId){
        payRecordService.lambdaUpdate().eq(PayRecord::getPayId,payId).remove();
    }

    /**
     * 本次收费-修改收费状态和收费方式
     */
    @Override
    public boolean updatePayById(UpdatePayById param) {
        return payService.updateById(payConverter.toPayEntity(param));
    }


    /**
     * 创建收费记录+处方收费项目
     */
    @Override
    public Long createPayAndPrescriptionRecord(Prescription prescription, Dossier dossier) {
        Long userId = LoginUser.getId();
        Pay pay = new Pay(userId,dossier,prescription);
        boolean b = payService.save(pay);
        boolean payDetails = payRecordService.save(new PayRecord(pay.getId(), prescription.getPrice(), userId));
        return b&payDetails? pay.getId():null;
    }

    /**
     * 修改处方收费项目
     */
    @Override
    public boolean updatePayPrescriptionRecord(Long payId, BigDecimal prescriptionPrice) {
        payService.lambdaUpdate().set(Pay::getFee,prescriptionPrice).eq(Pay::getId,payId).update();
        return payRecordService.lambdaUpdate().set(PayRecord::getFee,prescriptionPrice)
                .eq(PayRecord::getPayId,payId).eq(PayRecord::getName,"处方").update();
    }



    @Autowired
    public void setPayRecordService(PayService payService) {
        this.payService = payService;
    }
    @Autowired
    public void setPayDetailsService(PayRecordService payRecordService) {
        this.payRecordService = payRecordService;
    }
    @Autowired
    public void setPayConverter(PayConverter payConverter) {
        this.payConverter = payConverter;
    }

}
