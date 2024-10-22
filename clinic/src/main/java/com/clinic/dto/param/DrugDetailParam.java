package com.clinic.dto.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DrugDetailParam {

    /**
     * id
     */
    private Long id;

    /**
     * 药品名称
     */
    private String name;


    /**
     * 药品类型
     */
    private Integer type;

    /**
     * 规格
     */
    private String size;

    /**
     * 单位
     */
    private String unit;

    /**
     * 产地
     */
    private String origin;

    /**
     * 生产厂商
     */
    private String manufacturer;

    /**
     * 药品编号
     */
    private String drugNo;


    /**
     * 有效期
     */
    private String expiryDate;

    /**
     * 药品库存
     */
    private Long inventory;

    /**
     * 出/入库数量
     */
    private Long inOrOutQTY;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 出售单价
     */
    private BigDecimal sellingPrice;

    /**
     *  用户ID
     */
    private Long userId;

    /**
     * 创建人
     */
    private String createName;




}
