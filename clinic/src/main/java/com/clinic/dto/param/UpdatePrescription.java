package com.clinic.dto.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdatePrescription {

    /**
     * 接诊日志id
     */
    @NotNull(message = "接诊日志id不能为空！")
    private Long admissionId;

    /**
     *
     */
    @NotNull(message = "处方主键不能为空！")
    private Long prescriptionId;


    /**
     * 支付编号
     */
    @NotNull(message = "支付编号不能为空！")
    private Long payId;

    /**
     * 病列信息
     */
    @Valid
    private UpdateDossierParam dossier;

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
    private List<UpdatePrescriptionDrug> drugList;

}
