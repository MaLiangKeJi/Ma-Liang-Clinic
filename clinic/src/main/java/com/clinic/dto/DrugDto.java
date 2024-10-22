package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品表
 * @TableName drug
 */
@Data
public class DrugDto implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 药品类型:
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 剂型
     */
    @TableField(value = "size")
    private String size;

    /**
     * 规格
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 上市许可持有人
     */
    @TableField(value = "origin")
    private String origin;

    /**
     * 生产单位
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 药品编码
     */
    @TableField(value = "drug_no")
    private String drugNo;

    /**
     * 成本
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 药品编码备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 批准文号
     */
    @TableField(value = "approval_number")
    private String approvalNumber;


}