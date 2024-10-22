package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.dto.param.RecordAdmissionLogParam;
import com.clinic.enums.AdmissionStateEnum;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接诊日志
 */
@TableName(value ="log_admission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionLog implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private Long phone;

    /**
     * 住址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 出生年月
     */
    @TableField(value = "birth_date")
    private Date birthDate;

    /**
     * 病人ID
     */
    @TableField(value = "patient_id")
    private Long patientId;


    /**
     * 诊断
     */
    @TableField(value = "diagnosis")
    private String diagnosis;


    /**
     * 医生ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 就诊日期（创建日期）
     */
    @TableField(value = "create_time")
    private Date createTime;

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

    /**
     * 收费记录ID
     */
    @TableField(value = "pay_id")
    private Long payId;

    /**
     * 初复诊（0初诊/1复诊）
     */
    @TableField(value = "is_first")
    private Integer isFirst;

    /**
     * 接诊状态（0未接诊/1正在接诊/2结束就诊）
     */
    @TableField(value = "state")
    private AdmissionStateEnum state;

    /**
     * 关联微信编号
     */
    @TableField(value = "open_id")
    private String openId;

    /**
     * 是否由药房收费，0否1是
     */
    @TableField(exist = false)
    private Integer isPharmacyPay;


    @TableField(exist = false)
    private Patient patient;

    @TableField(exist = false)
    private Dossier dossier;

    @TableField(exist = false)
    private Prescription prescription;

    @TableField(exist = false)
    private List<PrescriptionDrug> prescriptionDrugs;

    @TableField(exist = false)
    private Pay pay;

    @TableField(exist = false)
    private PayRecord payRecords;

    /**
     * 就诊日期（创建日期）
     */
    @TableField(exist = false)
    private String createTimeStr;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public AdmissionLog(Patient patient, Long userId) {
        this.name = patient.getName();
        this.sex = patient.getSex();
        this.age = patient.getAge();
        this.phone = patient.getPhone();
        this.address = patient.getAddress();
        this.birthDate = patient.getBirthDate();
        this.patientId = patient.getId();
        this.userId = userId;
    }

    public AdmissionLog(RecordAdmissionLogParam param, Patient patient) {
        this.patientId = param.getPatientId();
        this.isFirst = param.getIsFirst();

        this.userId = LoginUser.getId();
        this.state = AdmissionStateEnum.RUN;

        this.name = patient.getName();
        this.sex = patient.getSex();
        this.age = patient.getAge();
        this.phone = patient.getPhone();
        this.address = patient.getAddress();
        this.birthDate = patient.getBirthDate();
        this.openId = patient.getOpenId();
    }
}