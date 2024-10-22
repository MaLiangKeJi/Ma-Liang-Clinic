package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 零售：关联药品记录
 * @TableName retail_drug_record
 */
@TableName(value ="retail_drug_record")
@Data
public class RetailDrugRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * retail_record ID
     */
    @TableField(value = "retail_id")
    private Long retailId;

    /**
     * 库存批次ID
     */
    @TableField(value = "stock_batch_id")
    private Long stockBatchId;

    /**
     * 药品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 批注文号
     */
    @TableField(value = "approval_number")
    private String approvalNumber;

    /**
     * 厂商名称
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 批次号
     */
    @TableField(value = "batch_number")
    private String batchNumber;

    /**
     * 生产日期
     */
    @TableField(value = "produce_date")
    private Date produceDate;

    /**
     * 过期日期
     */
    @TableField(value = "expiry_date")
    private Date expiryDate;

    /**
     * 剂型
     */
    @TableField(value = "dosage_form")
    private String dosageForm;

    /**
     * 规格
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 药品类型，取国药准字首字母
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 国家基本药物类型
     */
    @TableField(value = "essential")
    private Integer essential;

    /**
     * 皮试（0非皮试；1皮试）
     */
    @TableField(value = "skin_test")
    private Integer skinTest;

    /**
     * 库存量
     */
    @TableField(value = "stock_number")
    private Long stockNumber;

    /**
     * 库存单位（编号，最小计数单位）
     */
    @TableField(value = "stock_unit_id")
    private Integer stockUnitId;

    /**
     * 数量
     */
    @TableField(value = "number")
    private Integer number;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private RetailRecord retailRecord;

    /**
     * 是否是库存
     */
    @TableField(exist = false)
    private Boolean isStock;


    public void fillStockBatchInfo(StockBatch stockBatch) {
        this.name = stockBatch.getStock().getName();
        this.approvalNumber = stockBatch.getApprovalNumber();
        this.manufacturer = stockBatch.getManufacturer();
        this.batchNumber = stockBatch.getBatchNumber();
        this.produceDate = stockBatch.getProduceDate();
        this.expiryDate = stockBatch.getExpiryDate();
        this.dosageForm = stockBatch.getDosageForm();
        this.spec = stockBatch.getSpec();
        this.type = stockBatch.getType();
        this.essential = stockBatch.getEssential();
        this.skinTest = stockBatch.getSkinTest();
        this.stockNumber = stockBatch.getNumber();
        this.stockUnitId = stockBatch.getUnitId();
    }

    public void fillDrugInfo(Drug drug) {
        this.name = drug.getName();
        this.approvalNumber = drug.getApprovalNumber();
        this.manufacturer = drug.getManufacturer();
        this.dosageForm = drug.getType();
        this.spec = drug.getSpec();

    }
}