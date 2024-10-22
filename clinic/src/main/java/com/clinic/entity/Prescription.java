package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.annotation.EntityMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 处方
 * @TableName prescription
 */
@TableName(value = "prescription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class Prescription implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 病人编号
     */
    @TableField(value = "patient_id")
    private Long patientId;

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
    @TableField(value = "expiry_date")
    private Integer expiryDate;

    /**
     * 处方过期时间
     */
    @TableField(value = "expiration_date")
    private Date expirationDate;

    /**
     * 总价，药方药品总价之和
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 创建者（用户）编号
     */
    @TableField(value = "creator")
    private Long creator;

    /**
     * 修改者（用户）编号
     */
    @TableField(value = "updator")
    private Long updator;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 用法列表
     */
    @TableField(exist = false)
    private List<Usage> usageList;

    @TableField(exist = false)
    @EntityMapping(
            thisField = Prescription.Fields.id,
            joinField = PrescriptionDrug.Fields.prescriptionId
    )
    private List<PrescriptionDrug> drugs;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}