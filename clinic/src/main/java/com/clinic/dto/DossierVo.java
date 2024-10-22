package com.clinic.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class DossierVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 病人id
     */
    private Long patientId;

    /**
     * 既往史 (含药物过敏史)
     */
    private String pastMedicalHistory;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 体温
     */
    private Double temperature;

    /**
     * 现病史
     */
    private String historyPresentIllness;

    /**
     * 处理
     */
    private String dealWith;

    /**
     * 备注
     */
    private String notes;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}