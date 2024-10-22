package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.enums.PayWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费记录
 * @TableName pay_record
 */
@TableName(value ="pay")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pay implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 处方id
     */
    @TableField(value = "prescription_id")
    private Long prescriptionId;

    /**
     * 费用
     */
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 状态：0未支付，1已支付
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 收费方式：1微信，2支付宝，3挂账，4现金
     */
    @TableField(value = "way")
    private PayWay way;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
     * 就诊时间，既创建病例时间
     */
    @TableField(value = "dossier_time")
    private Date dossierTime;

    /**
     * 初步诊断
     */
    @TableField(value = "diagnosis")
    private String diagnosis;

    /**
     * 用户id
     */
    @TableField(value = "creator")
    private Long creator;

    /**
     * 病人编号
     */
    @TableField(value = "patient_id")
    private Long patientId;

    @TableField(exist = false)
    private Patient patient;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Pay(Long userId, Dossier dossier, Prescription prescription) {
        this.prescriptionId = prescription.getId();
        this.dossierTime = dossier.getCreateTime()==null?new Date():dossier.getCreateTime();
        this.diagnosis = dossier.getDiagnosis();
        this.creator = userId;
        this.fee = prescription.getPrice();
        this.patientId = prescription.getPatientId();
    }
}