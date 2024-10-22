package com.clinic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.dto.PrescriptionDto;
import com.clinic.dto.PrescriptionFileVo;
import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.entity.Prescription;
import com.clinic.entity.StockBatch;
import com.github.yulichang.base.MPJBaseService;

import java.io.IOException;
import java.util.List;

/**
 * 处方
 */
public interface PrescriptionService extends MPJBaseService<Prescription> {

    IPage<PrescriptionDto> selectPage(Long id, Long dossierId, Long patientId, Integer current, Integer size);

    List<PrescriptionDto> select(Long patientId);

    List<PrescriptionDto> select(Long id, List<Long> dossierIds);

    PrescriptionFileVo getFileInfo(Long id);

    PrescriptionDto getByPayId(Long payId);

    PrescriptionDto getByAdmissionId(Long admissionId);

    Page<PrescriptionSearchDrugVO> searchPrescriptionVO(String drugName);

    Page<StockBatch> searchStockBatch(String drugName);

    PrescriptionSearchDrugVO converter(StockBatch stockBatch);

    void download(Long id) throws IOException;
}
