package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;
import com.bbs.exception.BusinessException;
import com.clinic.dto.param.SaveOrUpdateDossierParam;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Dossier;

/**
 * 病历
 */
public interface DossierService extends IService<Dossier> {

    Dossier createOrUpdateDossier(Long admissionID, Long patientId, SaveOrUpdateDossierParam param) throws BusinessException;

    Dossier createOrUpdateDossier(AdmissionLog admissionLog, SaveOrUpdateDossierParam param) throws BusinessException;

    Result<Page<Dossier>> select(Long userId, String id, Integer current, Integer size);

}
