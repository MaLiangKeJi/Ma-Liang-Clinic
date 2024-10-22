package com.clinic.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.util.FirstWordsSqlUtils;
import com.clinic.cache.unit.UnitCache;
import com.clinic.converter.StockConverter;
import com.clinic.dto.PrescriptionDto;
import com.clinic.dto.PrescriptionFileVo;
import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Clinic;
import com.clinic.entity.Dossier;
import com.clinic.entity.DossierPrescription;
import com.clinic.entity.Patient;
import com.clinic.entity.Pay;
import com.clinic.entity.Prescription;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.entity.Unit;
import com.clinic.mapper.PrescriptionMapper;
import com.clinic.service.PrescriptionService;
import com.clinic.util.LoginUser;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Validator.isWord;
import static com.deepoove.poi.config.Configure.builder;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * 处方
 */
@Slf4j
@Service
public class PrescriptionServiceImpl extends MPJBaseServiceImpl<PrescriptionMapper, Prescription>
    implements PrescriptionService {

    @Resource
    private StockConverter converter;

    @Resource
    private UnitCache unitCache;

    @Override
    public IPage<PrescriptionDto> selectPage(Long id, Long dossierId, Long patientId, Integer current, Integer size) {
        return selectJoinListPage(new Page<>(current, size), PrescriptionDto.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .select(DossierPrescription::getDossierId)
                        .leftJoin(DossierPrescription.class, DossierPrescription::getPrescriptionId, Prescription::getId)
                        .eq(nonNull(id),Prescription::getCreator, id)
                        .eq(nonNull(patientId), Prescription::getPatientId, patientId)
                        .eq(nonNull(dossierId), DossierPrescription::getDossierId, dossierId));
    }

    @Override
    public List<PrescriptionDto> select(Long patientId) {
        return selectJoinList(PrescriptionDto.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .eq(nonNull(patientId), Prescription::getPatientId, patientId));
    }

    @Override
    public List<PrescriptionDto> select(Long id, List<Long> dossierIds) {
        return selectJoinList(PrescriptionDto.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .select(DossierPrescription::getDossierId)
                        .leftJoin(DossierPrescription.class, DossierPrescription::getPrescriptionId, Prescription::getId)
                        .eq(Prescription::getCreator, id)
                        .in(nonNull(dossierIds), DossierPrescription::getDossierId, dossierIds));
    }

    @Override
    public PrescriptionFileVo getFileInfo(Long id) {
        return selectJoinOne(PrescriptionFileVo.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .select(Patient::getName,Patient::getSex,Patient::getAge)
                        .select(Dossier::getPastMedicalHistory,Dossier::getDiagnosis)
                        .selectAs(Clinic::getName,PrescriptionFileVo::getClinicName)
                        .leftJoin(Patient.class,Patient::getId,Prescription::getPatientId)
                        .leftJoin(DossierPrescription.class,DossierPrescription::getPrescriptionId,Prescription::getId)
                        .leftJoin(Dossier.class,Dossier::getId,DossierPrescription::getDossierId)
                        .leftJoin(Clinic.class,Clinic::getUserId,Prescription::getCreator)
                        .eq(Prescription::getId,id)
        );
    }

    @Override
    public PrescriptionDto getByPayId(Long payId) {
        return selectJoinOne(PrescriptionDto.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .selectCollection(PrescriptionDrug.class,PrescriptionDto::getDrugList)
                        .leftJoin(PrescriptionDrug.class,PrescriptionDrug::getPrescriptionId,Prescription::getId)
                        .leftJoin(Pay.class,Pay::getPrescriptionId,Prescription::getId)
                        .eq(Pay::getId,payId));
    }

    @Override
    public PrescriptionDto getByAdmissionId(Long admissionId) {
        return selectJoinOne(PrescriptionDto.class,
                new MPJLambdaWrapper<Prescription>()
                        .selectAll(Prescription.class)
                        .selectCollection(PrescriptionDrug.class,PrescriptionDto::getDrugList)
                        .leftJoin(PrescriptionDrug.class,PrescriptionDrug::getPrescriptionId,Prescription::getId)
                        .leftJoin(AdmissionLog.class,AdmissionLog::getPrescriptionId,Prescription::getId)
                        .eq(AdmissionLog::getId,admissionId));
    }

    @Override
    public Page<PrescriptionSearchDrugVO> searchPrescriptionVO(String drugName) {
        Page<StockBatch> searchResult = searchStockBatch(drugName);

        List<PrescriptionSearchDrugVO> voList = searchResult.getRecords().stream().map(this::converter).collect(Collectors.toList());

        Page<PrescriptionSearchDrugVO> result = new Page<>(searchResult.getCurrent(), searchResult.getSize(), searchResult.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    public Page<StockBatch> searchStockBatch(String drugName) {
        MPJLambdaWrapper<StockBatch> wrapper = new MPJLambdaWrapper<>(StockBatch.class)
                .selectAll(StockBatch.class)

                .selectAs(Stock::getName, StockBatch::getName)
                .leftJoin(Stock.class, Stock::getId, StockBatch::getStockId)

                .leftJoin(StockUnit.class, StockUnit::getBatchId, StockBatch::getId, ext -> ext
                        .selectCollection(StockUnit.class, StockBatch::getStockUnitList)
                )
                // 不再关联单位表，转而从缓存获取（unit.table）

                .eq(Stock::getUserId, LoginUser.getId());
                if(StringUtils.isNotBlank(drugName)){
                    if(isWord(drugName)){
                        String sql = FirstWordsSqlUtils.getSql(drugName);
                        wrapper.apply(sql);
                    }else{
                        wrapper.likeRight(Stock::getName, drugName);
                    }
                }
        return wrapper.page(new Page<>(1, 10), StockBatch.class);
    }

    @Override
    public PrescriptionSearchDrugVO converter(StockBatch stockBatch) {
        PrescriptionSearchDrugVO vo = converter.toPrescriptionSearchDrugVO(stockBatch);
        vo.setName(stockBatch.getName());


        vo.setStockState(stockBatch.getState().getCode());

        vo.setExpiryDate(DateUtil.formatDate(stockBatch.getExpiryDate()));
        vo.setExpiryState(stockBatch.getExpiryState());

        List<Unit> units = unitCache.getUnit(Arrays.asList(stockBatch.getSingleDoseUnitId(), stockBatch.getUnitId()));
        Unit singleDoseUnit = units.get(INTEGER_ZERO);
        if(nonNull(singleDoseUnit)) vo.setSingleDoseUnit(singleDoseUnit.getName());
        Unit stockNumberUnit = units.get(INTEGER_ONE);
        if(nonNull(stockNumberUnit)) vo.setStockNumberUnit(stockNumberUnit.getName());

        return vo;
    }

    @Override
    public void download(Long id) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/template.docx");

        List<Content> contents = new ArrayList<>();
        contents.add(new Content("阿莫西林胶囊 0.36克 × 38 粒 × 3盒", "用法：0.72克 . 口服 . 每日三次"));
        contents.add(new Content("美洛昔康片 0.36克 × 38 粒 × 3盒", "用法：0.72克 . 口服 . 每日二次"));
        contents.add(new Content("以下内容为空", ""));
        contents.add(new Content("", ""));
        contents.add(new Content("", ""));

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = builder()
                .bind("content", policy)
                .build();

        XWPFTemplate template = XWPFTemplate.compile(resource.getAbsolutePath(), config).render(
                new HashMap<String, Object>(){{
                    put("title", "路通西医诊所");
                    put("a", "521465161615");
                    put("b", "路陈琳"); //name
                    put("c", "男"); //sex
                    put("d", "23岁"); //age
                    put("e", "12kg"); //体重
                    put("f", "无"); //过敏史
                    put("g", "2023-12-15"); //日期
                    put("diagnosis", "支气管哮喘");
                    put("content", contents);
                    put("doctor", "路通");
                    put("cost", "27.00");
                }});

        template.writeToFile("e:\\a.docx");
    }

    @Data
    @AllArgsConstructor
    private static class Content {
        private String drug;
        private String sig;
    }
}


