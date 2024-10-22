package com.clinic.dto.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SaveOrUpdatePrescriptionDrug {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批次编号
     */
    private Long stockBatchId;

    /**
     * 药品名称
     */
    @NotNull(message = "药品名称不能为空")
    private String name;

    /**
     * 规格
     */
    @NotNull(message = "规格不能为空")
    private String spec;

    /**
     * 单次剂量
     */
    @NotNull(message = "单次剂量不能为空")
    private String singleDose;

    /**
     * 单次剂量单位
     */
    private String singleDoseUnit;

    /**
     * 用法
     */
    @NotNull(message = "用法不能为空")
    private String drugUsage;

    /**
     * 用药频次
     */
    @NotNull(message = "用药频次不能为空")
    private String frequency;

    /**
     * 总量
     */
    @NotNull(message = "总量不能为空")
    private Long quantity;

    /**
     * 数量单位
     */
    @NotNull(message = "数量单位不能为空")
    private String quantityUnit;


    /**
     * 天数（周期）
     */
    @NotNull(message = "周期不能为空")
    private Integer period;

    /**
     * 天数（周期）单位
     */
    @NotNull(message = "天数（周期）单位不能为空")
    private String periodUnit;

    /**
     * 生产单位
     */
    @NotNull(message = "生产单位不能为空")
    private String manufacturer;

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
