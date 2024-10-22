package com.clinic.dto.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaveOrUpdatePrescription {

    /**
     * 接诊日志id
     */
    @NotNull(message = "接诊日志id不能为空！")
    private Long admissionLogId;

    /**
     *
     */
    private Long prescriptionId;

    /**
     * 支付编号
     */
    private Long payId;

    /**
     * 病列编号
     */
    @Valid
    private SaveOrUpdateDossierParam dossier;

    /**
     * 总价，药方药品总价之和
     */
    @NotNull(message = "总价不能为空！")
    private BigDecimal price;

    /**
     * 备注
     */
    private String remark;

    @Valid
    @NotNull
    private List<SaveOrUpdatePrescriptionDrug> drugList;
}
