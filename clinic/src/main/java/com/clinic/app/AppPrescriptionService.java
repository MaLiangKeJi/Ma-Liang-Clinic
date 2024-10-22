package com.clinic.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bbs.Result;
import com.clinic.dto.PrescriptionDrugDto;
import com.clinic.dto.PrescriptionDto;
import com.clinic.dto.param.SaveOrUpdatePrescription;
import com.clinic.entity.Prescription;
import com.clinic.entity.PrescriptionDrug;

import java.util.List;

public interface AppPrescriptionService {

    Prescription save(SaveOrUpdatePrescription param, Long patientId, Long dossierId);
    boolean update(SaveOrUpdatePrescription param);

    Result<IPage<PrescriptionDto>> selectOr(Long dossierId, Long patientId, Integer current, Integer size);

    List<PrescriptionDto> selectByIds(Long id, List<Long> dossierIds);

    PrescriptionDto getByPayId(Long payId);

    List<PrescriptionDrugDto> getDrugList(Long id);

    void updateDrug(List<PrescriptionDrug> drugList, Long id);
}
