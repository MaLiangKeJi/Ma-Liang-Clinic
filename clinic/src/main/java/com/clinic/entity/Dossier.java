package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.dto.PrescriptionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 病历
 * @TableName dossier
 */
@TableName(value ="dossier")
@Data
public class Dossier implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 病人id
     */
    @TableField(value = "patient_id")
    private Long patientId;

    /**
     * 既往史
     */
    @TableField(value = "past_medical_history")
    private String pastMedicalHistory;

    /**
     * 主诉
     */
    @TableField(value = "chief_complaint")
    private String chiefComplaint;

    /**
     * 诊断
     */
    @TableField(value = "diagnosis")
    private String diagnosis;

    /**
     * 现病史
     */
    @TableField(value = "history_present_illness")
    private String historyPresentIllness;

    /**
     * 查体
     */
    @TableField(value = "checkup")
    private String checkup;

    /**
     * 辅助检查
     */
    @TableField(value = "auxiliary_checkup")
    private String auxiliaryCheckup;


    /**
     * 处理
     */
    @TableField(value = "deal_with")
    private String dealWith;

    /**
     * 备注
     */
    @TableField(value = "notes")
    private String notes;

    /**
     * 创建人Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private PrescriptionDto prescriptionDto;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}