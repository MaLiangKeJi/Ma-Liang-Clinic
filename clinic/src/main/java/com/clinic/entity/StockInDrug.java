package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存：入库 & 药品关联
 * @TableName stock_in_drug
 */
@TableName(value ="stock_in_drug")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class StockInDrug implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入库编号
     */
    @TableField(value = "no")
    private String no;

    /**
     * 库存编号
     */
    @TableField(value = "stock_batch_id")
    private Long stockBatchId;

    /**
     * 入库单（编号）
     */
    @TableField(value = "stock_in_id")
    private Long stockInId;

    /**
     * 通用名称
     */
    @TableField(value = "name")
    private String name;

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
     * 批准文号
     */
    @TableField(value = "approval_number")
    private String approvalNumber;

    /**
     * 生产批号
     */
    @TableField(value = "batch_number")
    private String batchNumber;

    /**
     * 生产日期
     */
    @TableField(value = "produce_date")
    private Date produceDate;

    /**
     * 有效期
     */
    @TableField(value = "expiry_date")
    private Date expiryDate;

    /**
     * 生产厂商
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 供货单位
     */
    @TableField(value = "provider")
    private String provider;

    /**
     * 购货数量
     */
    @TableField(value = "number")
    private Long number;

    /**
     * 购货数量单位
     */
    @TableField(value = "unit_name")
    private String unitName;

    /**
     * 购进价格（成本单个）
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 单个单位
     */
    @TableField(value = "cost_unit")
    private Integer costUnit;

    /**
     * 总进价
     */
    @TableField(value = "total_cost")
    private BigDecimal totalCost;

    /**
     * 总进价单位
     */
    @TableField(value = "total_cost_unit")
    private Integer totalCostUnit;

    /**
     * 验收结论
     */
    @TableField(value = "accept_result")
    private String acceptResult;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 购货日期
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 验收人签名
     */
    @TableField(value = "create_name")
    private String createName;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



    public StockInDrug(String no, StockIn stockIn, Stock stock, StockBatch batch) {
        this(
                no,
                stockIn.getId(),
                stock.getName(),
                batch.getManufacturer(),
                batch.getProvider(),
                batch.getNumber(),
                batch.getUnitName(),
                batch.getCost(),
                batch.getProduceDate(),
                batch.getExpiryDate(),
                batch.getBatchNumber(),
                batch.getApprovalNumber(),
                batch.getDosageForm(),
                batch.getSpec()
        );
    }

    public StockInDrug(String no, Long stockInId, String name, String manufacturer, String provider, Long number, String unitName, BigDecimal cost, Date produceDate, Date expiryDate, String batchNumber, String approvalNumber, String dosageForm, String spec) {
        this.no = no;
        this.stockInId = stockInId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.provider = provider;
        this.number = number;
        this.unitName = unitName;
        this.cost = cost;
        this.produceDate = produceDate;
        this.expiryDate = expiryDate;
        this.batchNumber = batchNumber;
        this.approvalNumber = approvalNumber;
        this.dosageForm = dosageForm;
        this.spec = spec;
        this.userId = LoginUser.getId();
    }
}