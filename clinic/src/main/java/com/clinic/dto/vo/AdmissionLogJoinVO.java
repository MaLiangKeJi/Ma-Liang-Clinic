package com.clinic.dto.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.entity.Dossier;
import com.clinic.entity.Patient;
import com.clinic.entity.Prescription;
import com.clinic.entity.PrescriptionDrug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 接诊日志
 */
@TableName(value ="admission_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionLogJoinVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 住址
     */
    private String address;

    /**
     * 出生年月
     */
    private Date birthDate;

    /**
     * 病人ID
     */
    private Long patientId;


    /**
     * 诊断
     */
    private String diagnosis;


    /**
     * 医生ID
     */
    private Long userId;

    /**
     * 就诊日期（创建日期）
     */
    private Date createTime;

    /**
     * 病历ID
     */
    private Long dossierId;

    /**
     * 处方ID
     */
    private Long prescriptionId;

    /**
     * 收费记录ID
     */
    private Long payId;

    /**
     * 初复诊（0初诊/1复诊）
     */
    private Integer isFirst;

    /**
     * 接诊状态（0未接诊/1正在接诊/2结束就诊）
     */
    private Integer state;



    private Patient patient;

    private Dossier dossier;

    private Prescription prescription;

    private List<PrescriptionDrug> prescriptionDrugs;

    /**
     * 就诊日期（创建日期）
     */
    private String createTimeStr;


//    public AdmissionLog(Patient patient, Long userId) {
//        this.name = patient.getName();
//        this.sex = patient.getSex();
//        this.age = patient.getSex();
//        this.phone = patient.getPhone();
//        this.address = patient.getAddress();
//        this.birthDate = patient.getBirthDate();
//        this.patientId = patient.getId();
//        this.userId = userId;
//    }
//
//    public AdmissionLog(RecordAdmissionLogParam param, Patient patient) {
//        this.patientId = param.getPatientId();
//        this.isFirst = param.getIsFirst();
//
//        this.userId = LoginUser.getId();
//        this.state = AdmissionStateEnum.RUN.getCode();
//
//        this.name = patient.getName();
//        this.sex = patient.getSex();
//        this.age = patient.getSex();
//        this.phone = patient.getPhone();
//        this.address = patient.getAddress();
//        this.birthDate = patient.getBirthDate();
//    }
}
