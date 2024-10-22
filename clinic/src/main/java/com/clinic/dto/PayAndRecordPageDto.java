package com.clinic.dto;

import com.clinic.entity.Prescription;
import com.clinic.enums.PayWay;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 全部收费列表返回
 */
@Data
public class PayAndRecordPageDto {

    private Long id;

    /**
     * 就诊时间，既创建病例时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dossierTime;

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
     * 费用
     */
    private BigDecimal fee;

    private Integer state;

    /**
     * 收费方式
     */
    private PayWay way;

    /**
     * 门诊日志d
     */
    private Long admissionId;
    /**
     * 处方
     */
    private Prescription prescription;

}
