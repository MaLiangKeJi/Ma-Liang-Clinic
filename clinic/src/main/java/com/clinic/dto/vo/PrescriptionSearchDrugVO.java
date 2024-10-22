package com.clinic.dto.vo;

import com.clinic.entity.StockUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionSearchDrugVO {

    /**
     * 库存ID
     */
    private Long id;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 规格
     */
    private String spec;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 单次剂量
     */
    private Long singleDose;

    /**
     * 单次剂量
     */
    private String singleDoseUnit;

    /**
     * 用药频次
     */
    private String frequency;

    /**
     * 用法
     */
    private String drugUsage;

    /**
     * 皮试（0非皮试；1皮试）
     */
    private Integer skinTest;

    /**
     * 库存数量
     */
    private Long stockNumber;

    /**
     * 库存数量单位
     */
    private String stockNumberUnit;

    /**
     * 零售价
     */
    private BigDecimal price;

    /**
     * 零售价
     */
    private Long priceUnit;

    /**
     * 进价
     */
    private BigDecimal cost;

    /**
     * 库存状态
     */
    private Integer stockState;

    /**
     * 过期时间
     */
    private String expiryDate;

    /**
     * 过期状态
     */
    private Integer expiryState;

    /**
     * 单位列表
     */
    private List<Unit> units;

    /**
     * 单位列表
     */
    private List<StockUnit> stockUnitList;

    /**
     * 最小单位
     */
    private Unit minUnit;

    /**
     * 最大单位
     */
    private Unit maxUnit;

    /**
     * 是否是库存
     */
    private Boolean isStock;

    /**
     * 库存统计规则
     */
    private Integer stateCountRule;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Unit {

        private Integer id;

        private String name;

        private Long stepSize;
    }
}
