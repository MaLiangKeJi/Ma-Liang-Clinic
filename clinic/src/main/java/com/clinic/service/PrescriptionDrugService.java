package com.clinic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.entity.PrescriptionDrug;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

/**
 *
 */
public interface PrescriptionDrugService extends MPJBaseService<PrescriptionDrug> {

    List<PrescriptionDrug> searchDrug(Long prescriptionId);




}
