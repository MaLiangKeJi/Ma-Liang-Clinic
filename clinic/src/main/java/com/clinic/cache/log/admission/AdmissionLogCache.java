package com.clinic.cache.log.admission;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.dto.param.SearchAdmissionParam;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Patient;

import java.text.ParseException;

public interface AdmissionLogCache {

    Page<AdmissionLog> search(SearchAdmissionParam param) throws InterruptedException, ParseException;

    void remove();

    Long save(RecordAdmissionLogParam param);

    Long saveLogFormAddPatient(Patient patient);
}
