package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PrescriptionDto {

    /**
     * 编号
     */
    private Long id;

    /**
     * 病人编号
     */
    private Long patientId;

    /**
     * 病列编号
     */
    private Long dossierId;

    /**
     * 药物过敏史
     */
    @TableField(value = "drug_allergy_history")
    private String drugAllergyHistory;

    /**
     * 体重
     */
    @TableField(value = "weight")
    private Double weight;

    /**
     * 体温
     */
    @TableField(value = "temperature")
    private Double temperature;

    /**
     * 血压
     */
    @TableField(value = "blood_pressure")
    private Integer bloodPressure;

    /**
     * 血糖
     */
    @TableField(value = "blood_glucose")
    private Double bloodGlucose;

    /**
     * 处方有效期（处方一般不得超过 7 日用量；急诊处）
     */
    private Integer expiryDate;

    /**
     * 处方过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expirationDate;

    /**
     * 总价，药方药品总价之和
     */
    private BigDecimal price;

    /**
     * 创建者（用户）编号
     */
    private Long creator;

    /**
     * 备注
     */
    private String remark;


    private List<PrescriptionDrugDto> drugList;

}
