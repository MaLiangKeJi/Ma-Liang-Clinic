package com.clinic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存：入库 & 药品关联
 * @TableName stock_in_drug
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryStockInDrugDto {

    /**
     * id
     */
    private Long id;

    /**
     * 入库编号
     */
    private String no;

    /**
     * 入库单（编号）
     */
    private Long stockInId;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 厂商名称
     */
    private String manufacturer;

    /**
     * 供应商
     */
    private String provider;

    /**
     * 数量
     */
    private Long number;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 成本（单个）
     */
    private BigDecimal cost;

    /**
     * 成本单位（单价单位）
     */
    private Long costUnit;

    /**
     * 验收结论
     */
    private String acceptResult;

    /**
     * 总进价
     */
    private BigDecimal totalCost;

    /**
     * 总进价单位
     */
    private Long totalCostUnit;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date produceDate;

    /**
     * 有效期至
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expiryDate;

    /**
     * 产品批号
     */
    private String batchNumber;

    /**
     * 批注文号
     */
    private String approvalNumber;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 规格
     */
    private String spec;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    /**
     * 验收人签名
     */
    private String createName;

    /**
     * 备注
     */
    private String remark;
}