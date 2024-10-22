package com.clinic.dto.vo;

import lombok.Data;

@Data
public class DiagnosisProofFileVo {
    /**
     * 编号
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
     * 就诊时间
     */
    private String visitDate;

    /**
     * 住址
     */
    private String address;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 处理
     */
    private String dealWith;


    /**
     * 诊所名称
     */
    private String clinicName;

}
