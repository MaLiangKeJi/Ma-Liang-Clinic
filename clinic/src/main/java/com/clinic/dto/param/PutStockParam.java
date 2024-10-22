package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutStockParam {

    //1.药品信息

    /**
     *通用名称
     */
    @NotNull(message = "通用名称不能为空！")
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 批准文号
     */
    @NotNull(message = "批准文号不能为空！")
    private String approvalNumber;

    /**
     * 生产厂商
     */
    @NotNull(message = "生产厂商不能为空！")
    private String manufacturer;

    /**
     * 生产批号
     */
    @NotNull(message = "生产批号不能为空！")
    private String batchNumber;

    /**
     *生产日期
     */
    @NotNull(message = "生产日期不能为空！")
    private Date produceDate;

    /**
     *有效期
     */
    @NotNull(message = "有效期不能为空！")
    private Date expiryDate;

    /**
     *剂型
     */
    @NotNull(message = "剂型不能为空！")
    private String type;

    /**
     *规格
     */
    @NotNull(message = "规格不能为空！")
    private String spec;

    /**
     *类型（如化学制剂）
     */
    @NotNull(message = "类型不能为空！")
    private String sort;

    /**
     *国家基本药物类型
     */
    @NotNull(message = "国家基本药物类型不能为空！")
    private String essential;


    /**
     *2.单位结构
     */
    @Valid
    @NotNull(message = "单位结构不能为空！")
    private List<StockUnitParam> stockUnit;


    //3.用法

    /**
     *单次剂量
     */
    @NotNull(message = "单次剂量不能为空！")
    private String singleDose;

    /**
     *单次剂量单位
     */
    @NotNull(message = "单次剂量单位不能为空！")
    private String singleDoseUnit;

    /**
     *频次
     */
    @NotNull(message = "频次不能为空！")
    private String frequency;

    /**
     *用法
     */
    @NotNull(message = "用法不能为空！")
    private String usage;

    /**
     *皮试
     */
    @NotNull(message = "皮试不能为空！")
    private Integer skinTest;


    //4.进货价格

    /**
     * 总进货价
     */
    @NotNull(message = "总进货价不能为空！")
    private BigDecimal totalCost;

    /**
     *入库数量
     */
    @NotNull(message = "入库数量不能为空！")
    private Long number;

    /**
     *单位名称
     */
    @NotNull(message = "单位名称不能为空！")
    private String unitName;

    /**
     *单位id
     */
    @NotNull(message = "单位id不能为空！")
    private Long unitId;

    /**
     *单价
     */
    @NotNull(message = "单价不能为空！")
    private BigDecimal cost;

    /**
     *售价
     */
    @NotNull(message = "售价不能为空！")
    private BigDecimal price;

    /**
     *售价单位
     */
    @NotNull(message = "售价单位不能为空！")
    private Long priceUnit;

    /**
     *供货单位
     */
    private String provider;


    //5.缺货提醒

    /**
     * 统计类型
     */
    @NotNull(message = "统计类型不能为空！")
    private Integer countType;

    /**
     * 统计数量
     */
    @NotNull(message = "统计数量不能为空！")
    private Integer countNumber;

    /**
     * 统计单位
     */
    @NotNull(message = "统计单位不能为空！")
    private Integer countUnitId;


    /**
     * 验收结论
     */
    @NotNull(message = "验收结论不能为空！")
    private String acceptResult;

    /**
     * 备注
     */
    private String remark;


    @Data
    public static class StockUnitParam {

        /**
         * 单位编号
         */
        @NotNull(message = "单位编号不能为空！")
        private Integer id;

        /**
         * 1个父级单位对应的本单位数量(如1箱对应8瓶); 默认0代表最大单位
         */
        @NotNull(message = "单位数量不能为空！")
        private Long stepSize;

    }
}
