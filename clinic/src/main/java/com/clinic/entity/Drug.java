package com.clinic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName drug
 */
@TableName(value ="drug")
@Data
@Accessors(chain = true)
public class Drug implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称", orderNum = "2",fixedIndex = 2)
    @TableField(value = "name")
    private String name;

    /**
     * 拼音全拼
     */
    @TableField(value = "pin_yin")
    private String pinYin;

    /**
     * 拼音首字母
     */
    @TableField(value = "pin_yin_first_letter")
    private String pinYinFirstLetter;

    /**
     * 剂型（药品类型）
     */
    @Excel(name = "剂型", orderNum = "3",fixedIndex = 3)
    @TableField(value = "type")
    private String type;

    /**
     * 规格
     */
    @Excel(name = "规格", orderNum = "4",fixedIndex = 4)
    @TableField(value = "spec")
    private String spec;

    /**
     * 上市许可持有人
     */
    @Excel(name = "上市许可持有人", orderNum = "5",fixedIndex = 5)
    @TableField(value = "origin")
    private String origin;

    /**
     * 生产单位
     */
    @Excel(name = "生产单位", orderNum = "6",fixedIndex = 6)
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 药品编码
     */
    @Excel(name = "药品编码", orderNum = "7",fixedIndex = 7)
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
    @Excel(name = "药品编码备注", orderNum = "8",fixedIndex = 8)
    @TableField(value = "remark")
    private String remark;

    /**
     * 批准文号
     */
    @Excel(name = "批准文号", orderNum = "1",fixedIndex = 1)
    @TableField(value = "approval_number")
    private String approvalNumber;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}