package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 病历 & 处方
 * @TableName dossier_prescription
 */
@TableName(value ="dossier_prescription")
@Data
public class DossierPrescription implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 病历ID
     */
    @TableField(value = "dossier_id")
    private Long dossierId;

    /**
     * 处方ID
     */
    @TableField(value = "prescription_id")
    private Long prescriptionId;

    public DossierPrescription(Long dossierId, Long prescriptionId) {
        this.dossierId = dossierId;
        this.prescriptionId = prescriptionId;
    }

    public DossierPrescription(Prescription prescription, Dossier dossier) {
        this.dossierId = dossier.getId();
        this.prescriptionId = prescription.getId();
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}