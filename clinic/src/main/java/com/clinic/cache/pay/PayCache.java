package com.clinic.cache.pay;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.PayRecordPatientDto;
import com.clinic.dto.param.PatientPayRecordParam;
import com.clinic.dto.param.UpdatePayById;
import com.clinic.entity.Dossier;
import com.clinic.entity.Prescription;

public interface PayCache {
    Result<Page<PayRecordPatientDto>> selectPayPatient(PatientPayRecordParam param) throws InterruptedException;

    Long createPayAndPrescriptionRecord(Prescription save, Dossier dossier);

    boolean updatePayById(UpdatePayById param);

}
