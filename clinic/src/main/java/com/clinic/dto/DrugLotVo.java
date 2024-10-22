package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品批次+药品
 */
@Data
public class DrugLotVo implements Serializable {



    /**
     * 药品名称
     */
    private String drugName;

    /**
     * 药品类型
     */
    private Integer drugType;

    /**
     * 规格
     */
    private String drugSize;

    /**
     * 单位
     */
    private String drugUnit;

    /**
     * 产地
     */
    private String drugOrigin;

    /**
     * 生产厂商
     */
    private String drugManufacturer;

    /**
     * 药品编号
     */
    private String drugNo;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 有效期
     */
    private String expiryDate;


    /**
     * 药品库存
     */
    private Long inventory;

    /**
     * 出售单价
     */
    private BigDecimal sellingPrice;

    /**
     * 出售合计
     */
    private BigDecimal totalSellingPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 药品批次编号
     */
    private String lotNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}