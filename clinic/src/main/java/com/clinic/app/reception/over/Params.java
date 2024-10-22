package com.clinic.app.reception.over;

import com.clinic.dto.param.SaveOrUpdateDossierParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {

    /**
     * 接诊记录 ID
     */
    private Long admissionIds;

    /**
     * 病例
     */
    private SaveOrUpdateDossierParam dossier;

    /**
     * 处方
     */
    private PrescriptionParam prescription;
}