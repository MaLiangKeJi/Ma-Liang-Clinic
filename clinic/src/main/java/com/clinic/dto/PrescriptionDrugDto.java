package com.clinic.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrescriptionDrugDto {

    /**
     * 编号
     */
    private Long id;

    /**
     * 库存id
     */
    private Long stockBatchId;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 药品剂型
     */
    private String spec;

    /**
     * 单次剂量
     */
    private String singleDose;

    /**
     * 单次剂量单位
     */
    private String singleDoseUnit;

    /**
     * 用法
     */
    private String drugUsage;

    /**
     * 总量
     */
    private Long quantity;

    /**
     * 总量单位
     */
    private String quantityUnit;


    /**
     * 用药频次
     */
    private String frequency;

    /**
     * 天数（周期）
     */
    private String period;

    /**
     * 天数（周期）单位
     */
    private String periodUnit;

    /**
     * 最新库存数
     */
    private Long number;

    /**
     * 成本
     */
    private BigDecimal cost;


    /**
     * 单价
     */
    private BigDecimal price;


    /**
     * 备注
     */
    private String remark;



}
