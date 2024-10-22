package com.clinic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.cache.log.admission.AdmissionLogCache;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.dto.param.SearchAdmissionParam;
import com.clinic.dto.vo.PatientClinicVo;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Dossier;
import com.clinic.entity.Patient;
import com.clinic.entity.Pay;
import com.clinic.entity.Prescription;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.entity.Settings;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.entity.Unit;
import com.clinic.enums.AdmissionStateEnum;
import com.clinic.mapper.AdmissionLogMapper;
import com.clinic.service.AdmissionLogService;
import com.clinic.service.PatientService;
import com.clinic.service.PayRecordService;
import com.clinic.util.LoginUser;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
* @author 路晨霖
* @description 针对表【admission_log(接诊日志)】的数据库操作Service实现
* @createDate 2023-11-03 18:41:33
*/
@Service
public class AdmissionLogServiceImpl extends MPJBaseServiceImpl<AdmissionLogMapper, AdmissionLog>
    implements AdmissionLogService{


    private PatientService patientService;

    @Lazy
    @Resource
    private AdmissionLogCache admissionLogCache;

    @Resource
    private PayRecordService payRecordService;

    @Override
    public Page<AdmissionLog> search(SearchAdmissionParam param) throws ParseException {
        MPJLambdaWrapper<AdmissionLog> wrapper = new MPJLambdaWrapper<>(AdmissionLog.class);

        if(nonNull(param.getId())) {
            wrapper.eq(AdmissionLog::getId, param.getId());
        } else {
            addNameOrPhoneCondition(param, wrapper);

            if(nonNull(param.getCreateTime())) addDateCondition(param, wrapper);
        }
        wrapper.orderByDesc(AdmissionLog::getCreateTime).eq(AdmissionLog::getUserId, LoginUser.getId());
        Page<AdmissionLog> page = wrapper.page(param.toPage());
        page.getRecords().forEach(item -> item.setCreateTimeStr(DateUtil.formatDateTime(item.getCreateTime())));
        return page;
    }

    private void addNameOrPhoneCondition(SearchAdmissionParam param, MPJLambdaWrapper<AdmissionLog> wrapper) {
        String value = param.getValue();
        if(StringUtils.isNotBlank(value) && (!StringUtils.equals("null", value))) {
            if(isNumeric(value)) {
                wrapper.likeRight(Patient::getPhone, value);
            } else {
                wrapper.likeRight(Patient::getName, value);
            }
        }
    }

    private void addDateCondition(SearchAdmissionParam param, MPJLambdaWrapper<AdmissionLog> wrapper) throws ParseException {
        Date date = DateUtils.parseDate(param.getCreateTime(),"YYYY-MM-dd");
        Date beginOfDay = DateUtil.beginOfDay(date);
        Date endOfDay = DateUtil.endOfDay(date);
        wrapper.between(AdmissionLog::getCreateTime, beginOfDay, endOfDay);
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    @Override
    public boolean updateEndState(Long admissionId) {
        return lambdaUpdate().set(AdmissionLog::getState, AdmissionStateEnum.END.getCode()).eq(AdmissionLog::getId, admissionId).update();
    }


    @Override
    public List<PatientClinicVo> selectByOpenId(String openId) {
        return selectJoinList(PatientClinicVo.class, new MPJLambdaWrapper<>(AdmissionLog.class)
                .select("t.id","t.create_time","s.clinic_name","s.physician","d.chief_complaint")
                .leftJoin(Settings.class, "s", Settings::getUserId, AdmissionLog::getUserId)
                .leftJoin(Patient.class, Patient::getId, AdmissionLog::getPatientId)
                .leftJoin(Dossier.class,"d", Dossier::getId, AdmissionLog::getDossierId)
                .eq(Patient::getOpenId, openId)
                .eq(AdmissionLog::getState, NumberUtils.INTEGER_TWO)
        );
    }

    @Override
    public AdmissionLog getJoinById(Long admissionId) {
        MPJLambdaWrapper<AdmissionLog> wrapper = Optional.of(new MPJLambdaWrapper<>(AdmissionLog.class))
                .map(this::joinPatient)
                .map(this::joinDossier)
                .map(this::joinPrescription)
                .map(this::joinPrescriptionDrugAndStockBatch)
                .map(this::joinPay)
                .get();
        wrapper
                .eq(AdmissionLog::getId, admissionId)
                .orderByDesc(AdmissionLog::getCreateTime)
        ;

        AdmissionLog log = baseMapper.selectJoinOne(AdmissionLog.class, wrapper);
        if(nonNull(log.getPayId())) log.setPayRecords(payRecordService.getByPayId(log.getPayId()));

        return log;
    }



    private MPJLambdaWrapper<AdmissionLog> joinPatient(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectAssociation(Patient.class, AdmissionLog::getPatient)
                .leftJoin(Patient.class, Patient::getId, AdmissionLog::getPatientId);
        return wrapper;
    }
    private MPJLambdaWrapper<AdmissionLog> joinDossier(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectAssociation(Dossier.class, AdmissionLog::getDossier)
                .leftJoin(Dossier.class, Dossier::getId, AdmissionLog::getDossierId);
        return wrapper;
    }
    private MPJLambdaWrapper<AdmissionLog> joinPrescription(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectAssociation(Prescription.class, AdmissionLog::getPrescription)
                .leftJoin(Prescription.class, Prescription::getId, AdmissionLog::getPrescriptionId);
        return wrapper;
    }
    private MPJLambdaWrapper<AdmissionLog> joinPrescriptionDrugAndStockBatch(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectCollection(PrescriptionDrug.class, AdmissionLog::getPrescriptionDrugs, prescriptionDrug -> prescriptionDrug
                        .association(StockBatch.class, PrescriptionDrug::getStockBatch, stockBatch -> stockBatch
                                .association("unit1", Unit.class, StockBatch::getUnit)
                                .collection(StockUnit.class, StockBatch::getStockUnitList, stockUnit ->
                                        stockUnit.association("unit2", Unit.class, StockUnit::getUnit)
                                )

                        )
                        .association(Stock.class, PrescriptionDrug::getStock)
                )
                .leftJoin(PrescriptionDrug.class, PrescriptionDrug::getPrescriptionId, Prescription::getId)
                .leftJoin(StockBatch.class, StockBatch::getId, PrescriptionDrug::getStockBatchId)
                .leftJoin(Unit.class, "unit1", Unit::getId, StockBatch::getUnitId)
                .leftJoin(StockUnit.class, StockUnit::getBatchId, StockBatch::getId)
                .leftJoin(Unit.class, "unit2", Unit::getId, StockUnit::getUnitId)
                .leftJoin(Stock.class, Stock::getId, StockBatch::getStockId)
        ;
        return wrapper;
    }

    private MPJLambdaWrapper<AdmissionLog> joinPay(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectAssociation(Pay.class, AdmissionLog::getPay)
                .leftJoin(Pay.class, Pay::getPrescriptionId, Prescription::getId)
        ;
        return wrapper;
    }



    @Override
    public Long save(RecordAdmissionLogParam param) {
        Long loginUID = LoginUser.getId();
        Patient patient = patientService.getById(param.getPatientId());
        if (StrUtil.isNotBlank(patient.getOpenId())) {
            patient.setOpenId(param.getOpenId());
            patientService.updateById(patient);
        }
        AdmissionLog log = new AdmissionLog(param, patient);
        if(isNull(param.getIsFirst())) log.setIsFirst(computeIsFirst(param, loginUID));
        save(log);
        return log.getId();
    }

    @Override
    public void update(Long id, Long prescriptionId, Long payId, Long dossierId,String diagnosis) {
        lambdaUpdate()
                .set(AdmissionLog::getPrescriptionId, prescriptionId)
                .set(AdmissionLog::getPayId, payId)
                .set(AdmissionLog::getDossierId, dossierId)
                .set(AdmissionLog::getDiagnosis, diagnosis)
                .eq(AdmissionLog::getId, id).update();
        admissionLogCache.remove();
    }


    private Integer computeIsFirst(RecordAdmissionLogParam param, Long loginUID) {
        return (
                lambdaQuery()
                        .eq(AdmissionLog::getUserId, loginUID)
                        .eq(AdmissionLog::getPatientId, param.getPatientId())
                        .count() > 0
                ) ? 1 : 0;
    }


    @Lazy
    @Resource
    public void setPatientService(PatientService patientService){
        this.patientService = patientService;
    }
}




