package com.clinic.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PrescriptionFileVo {

    /**
     * 处方编号
     */
    private Long id;

    /**
     * 病人姓名
     */
    private String name;

    /**
     * 病人性别
     */
    private Integer sex;

    /**
     * 病人年龄
     */
    private Integer age;


    /**
     * 既往史 (含药物过敏史)
     */
    private String pastMedicalHistory = "无";

    /**
     * 体重
     */
    private Double weight;

    /**
     * 处方过期时间
     */
    private String expirationDate;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 诊所名称
     */
    private String clinicName;

    /**
     * 总价，药方药品总价之和
     */
    private BigDecimal price;



}
