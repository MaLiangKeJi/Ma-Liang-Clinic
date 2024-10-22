package com.clinic.dto.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PatientClinicVo {

    /**
     * 门诊日志id
     */
    private Long id;

    /**
     * 门诊时间
     */
    private Date createTime;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 诊所名称
     */
    private String clinicName;

    /**
     * 医生名称
     */
    private String physician;


}
