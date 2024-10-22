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
 * 药品批次详情表
 * @TableName drug_detail
 */
@TableName(value ="drug_detail")
@Data
public class DrugDetail implements Serializable {

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     *  用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 药品批次编号
     */
    @TableField(value = "lot_no")
    private String lotNo;

    /**
     * 药品库存
     */
    @TableField(value = "inventory")
    private Long inventory;

    /**
     * 出/入库数量
     */
    @TableField(value = "in_or_out_qty")
    private Long inOrOutQTY;

    /**
     * 出售单价
     */
    @TableField(value = "selling_price")
    private BigDecimal sellingPrice;

    /**
     * 出售合计
     */
    @TableField(value = "total_selling_price")
    private BigDecimal totalSellingPrice;

    /**
     * 药品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 药品类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 规格
     */
    @TableField(value = "size")
    private String size;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 产地
     */
    @TableField(value = "origin")
    private String origin;

    /**
     * 生产厂商
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 药品编号
     */
    @TableField(value = "drug_no")
    private String drugNo;

    /**
     * 成本
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 有效期
     */
    @TableField(value = "expiry_date")
    private String expiryDate;

    /**
     * 成本总价
     */
    @TableField(value = "total_cost")
    private BigDecimal totalCost;

    /**
     * 删除状态：0未删除  1已删除
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    public DrugDetail(Long id, Long inventory, BigDecimal cost, BigDecimal sellingPrice) {
        this.id = id;
        this.inventory = inventory;
        this.cost = cost;
        this.sellingPrice = sellingPrice;
    }

    public DrugDetail() {
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}