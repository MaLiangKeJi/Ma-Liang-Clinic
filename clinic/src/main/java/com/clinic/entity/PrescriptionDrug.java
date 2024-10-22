package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 处方药品表
 * @TableName prescription_drug
 */
@TableName(value ="prescription_drug")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class PrescriptionDrug implements Serializable {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 处方编号
     */
    @TableField(value = "prescription_id")
    private Long prescriptionId;

    /**
     * 批次编号
     */
    @TableField(value = "stock_batch_id")
    private Long stockBatchId;

    /**
     * 药品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 规格
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 单次剂量
     */
    @TableField(value = "single_dose")
    private Integer singleDose;

    /**
     * 单次剂量单位
     */
    @TableField(value = "single_dose_unit")
    private String singleDoseUnit;

    /**
     * 用法
     */
    @TableField(value = "drug_usage")
    private String drugUsage;

    /**
     * 用药频次
     */
    @TableField(value = "frequency")
    private String frequency;

    /**
     * 总量
     */
    @TableField(value = "quantity")
    private Long quantity;

    /**
     * 总量单位
     */
    @TableField(value = "quantity_unit")
    private String quantityUnit;

    /**
     * 天数（周期）
     */
    @TableField(value = "period")
    private Integer period;

    /**
     * 天数（周期）单位
     */
    @TableField(value = "period_unit")
    private String periodUnit;

    /**
     * 生产单位
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 成本
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 最新库存数
     */
    @TableField(exist = false)
    private Long number;

    /**
     * 数量单位
     */
    @TableField(exist = false)
    private Unit quantityUnitObj;

    @TableField(exist = false)
    private List<StockBatch> batchList;

    @TableField(exist = false)
    private StockBatch stockBatch;

    @TableField(exist = false)
    private Stock stock;

    @TableField(exist = false)
    private Unit singleDoseUnitObj;

    @TableField(exist = false)
    private List<Unit> units;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}