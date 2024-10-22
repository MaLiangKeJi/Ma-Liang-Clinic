package com.clinic.app.log.admission;

import cn.hutool.core.date.DateUtil;
import com.bbs.Result;
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
import com.clinic.mapper.AdmissionLogMapper;
import com.clinic.service.PayRecordService;
import com.clinic.util.LoginUser;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController("searchAdmissionJoin")
@RequestMapping
public class Search extends MPJBaseServiceImpl<AdmissionLogMapper, AdmissionLog> {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        /**
         * 日志记录 ID
         */
        @NotNull
        private Long id;
    }


    @Resource
    private PayRecordService payRecordService;

    /**
     * 查询就诊日志
     * @param param SearchAdmissionParam
     */
    @GetMapping("/log/admission/join")
    public Result<AdmissionLog> search(Param param) {
        MPJLambdaWrapper<AdmissionLog> wrapper = Optional.of(new MPJLambdaWrapper<>(AdmissionLog.class))
                .map(this::joinSetting)
                .map(this::joinPatient)
                .map(this::joinDossier)
                .map(this::joinPrescription)
                .map(this::joinPrescriptionDrugAndStockBatch)
                .map(this::joinPay)
                .get();
        wrapper
                .eq(AdmissionLog::getUserId, LoginUser.getId())
                .eq(AdmissionLog::getId, param.getId())
                .orderByDesc(AdmissionLog::getCreateTime)
        ;

        AdmissionLog log = baseMapper.selectJoinOne(AdmissionLog.class, wrapper);


        if(nonNull(log)) {
            log.setCreateTimeStr(DateUtil.formatDateTime(log.getCreateTime()));

            List<PrescriptionDrug> prescriptionDrugs = log.getPrescriptionDrugs();

            if(prescriptionDrugs.size() > INTEGER_ZERO) {
                log.getPrescriptionDrugs().forEach(prescriptionDrug -> {
                    StockBatch stockBatch = prescriptionDrug.getStockBatch();
                    List<Unit> units = new ArrayList<>();
                    if(nonNull(stockBatch)) {
                        List<StockUnit> stockUnitList = stockBatch.getStockUnitList();
                        if(nonNull(stockUnitList) && stockUnitList.size() > INTEGER_ZERO) {
                            stockUnitList.forEach(stockUnit -> {
                                Unit unit = stockUnit.getUnit();
                                unit.setSort(stockUnit.getSort());
                                unit.setStepSize(stockUnit.getStepSize());
                                units.add(unit);
                            });
                        }
                    }
                    prescriptionDrug.setUnits(units);
                });
            }
            if(nonNull(log.getPayId())) log.setPayRecords(payRecordService.getByPayId(log.getPayId()));
        }
        return Result.success(log);
    }

    private MPJLambdaWrapper<AdmissionLog> joinSetting(MPJLambdaWrapper<AdmissionLog> wrapper) {
        wrapper
                .selectAssociation(Settings.class, AdmissionLog::getIsPharmacyPay,e->e.result(Settings::getIsPharmacyPay))
                .leftJoin(Settings.class, Settings::getUserId, AdmissionLog::getUserId);
        return wrapper;
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
}
