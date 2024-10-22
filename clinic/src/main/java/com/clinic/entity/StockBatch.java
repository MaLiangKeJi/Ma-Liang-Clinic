package com.clinic.entity;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.app.AppStockService;
import com.clinic.dto.PrescriptionDrugDto;
import com.clinic.enums.DrugExpiryStateEnum;
import com.clinic.enums.DrugStockRule;
import com.clinic.enums.DrugTypeEnum;
import com.clinic.enums.StockStateEnum;
import com.clinic.service.StockUnitService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.clinic.enums.DrugExpiryStateEnum.ABOUT_EXPIRES;
import static com.clinic.enums.DrugExpiryStateEnum.EXPIRES;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * 
 * @TableName stock_batch
 */
@TableName(value ="stock_batch")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StockBatch implements Serializable {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 库存编号
     */
    @TableField(value = "stock_id")
    private Long stockId;

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
     * 过期状态（0正常、1过期）
     */
    @TableField(value = "expiry_state")
    private Integer expiryState;

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
     * 药品类型
     */
    @TableField(exist = false)
    private DrugTypeEnum typeObj;

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
     *售价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     *售价单位
     */
    @TableField(value = "price_unit")
    private Long priceUnit;

    /**
     * 用法
     */
    @TableField(value = "drug_usage")
    private String drugUsage;

    /**
     * 单次剂量
     */
    @TableField(value = "single_dose")
    private Integer singleDose;

    /**
     * 单次剂量单位
     */
    @TableField(value = "single_dose_unit")
    private Integer singleDoseUnitId;

    /**
     * 单次剂量单位
     */
    @TableField(exist = false)
    private Unit singleDoseUnit;

    /**
     * 频次
     */
    @TableField(value = "frequency")
    private String frequency;

    /**
     * 库存量
     */
    @TableField(value = "number")
    private Long number;

    /**
     * 库存单位（编号，最小计数单位）
     */
    @TableField(value = "unit_id")
    private Integer unitId;

    /**
     * 库存单位（编号，最小计数单位）
     */
    @TableField(exist = false)
    private Unit unit;

    /**
     * 总数（最近一次入库时候的存量 + 入库量）
     */
    @TableField(value = "total_number")
    private Long totalNumber;

    /**
     * 库存统计规则
     */
    @TableField(value = "state_count_rule")
    private DrugStockRule stateCountRule;

    /**
     * 统计值（统计方式值，如百分比 10%； 数量）
     */
    @TableField(value = "count_val")
    private Integer countVal;

    /**
     * 统计单位（编号）
     */
    @TableField(value = "count_unit_id")
    private Integer countUnitId;

    @TableField(exist = false)
    private Unit countUnit;

    /**
     * 库存状态
     */
    @TableField(value = "state")
    private StockStateEnum state;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 库存
     */
    @TableField(exist = false)
    private Stock stock;

    /**
     * 药品名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 单位列表
     */
    @TableField(exist = false)
    private List<StockUnit> stockUnitList;

    /**
     * 单位列表
     */
    @TableField(exist = false)
    private List<Unit> units;

    @TableField(exist = false)
    private List<Integer> stockUnitIds;

    @TableField(exist = false)
    private List<Drug> drug;

    /**
     * 供应商
     */
    @TableField(exist = false)
    private String provider;

    /**
     * 单位名称
     */
    @TableField(exist = false)
    private String unitName;

    /**
     * 进价
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 进价单位
     */
    @TableField(value = "cost_unit")
    private Integer costUnitId;

    /**
     * 进价单位
     */
    @TableField(exist = false)
    private Unit costUnit;
    /**
     * 药品类型（字母）
     */
    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 入库信息
     */
    @TableField(exist = false)
    private List<StockInDrug> stockInDrugList;

    public StockBatch(PrescriptionDrugDto drug, Long oldStockBatchNumber) {
        this.id = drug.getStockBatchId();
        this.number = oldStockBatchNumber-drug.getQuantity();
    }

    public StockBatch(Long id) {
        this.id = id;
    }


    public StockBatch(Long id, Long number) {
        this.id = id;
        this.number = number;
    }

    public StockStateEnum computeStockState() {
        StockUnitService stockUnitService = SpringUtil.getBean(StockUnitService.class);
        // 筛选出【库存状态】正常，且需要统计【库存预警状态】的库存批次
        if(StockStateEnum.NORMAL.equals(state) && !DrugStockRule.NOT_COUNT.equals(stateCountRule)) {
            // 当库存数量不足【自定义阈值】时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
            if (DrugStockRule.MIN_UNIT_PERCENTAGE_CUSTOMIZE.equals(stateCountRule)) {
                // 统计单位与库存单位是否一致，如果一致可以直接计算，否则需要将数量换算到一致的单位
                if (countUnitId.equals(unitId)) {
                    if (number <= countVal) {
                        return StockStateEnum.SHORTAGE;
                    }
                } else {
                    // 取出当前库存批次，对应的库存单位（包含全部单位与进制）
                    List<StockUnit> currentStockBatchUnits = stockUnitService.lambdaQuery()
                            .eq(StockUnit::getBatchId, id).orderByAsc(StockUnit::getSort).list();
                    // 获取统计单位与库存单位
                    StockUnit countUnit = null;
                    StockUnit stockNumberUnit = null;
                    for (StockUnit stockUnit : currentStockBatchUnits) {
                        if (stockUnit.getUnitId().equals(countUnitId)) {
                            countUnit = stockUnit;
                        }
                        if (stockUnit.getUnitId().equals(unitId)) {
                            stockNumberUnit = stockUnit;
                        }
                    }
                    // 设置了统计单位后，才能进行计算
                    if (nonNull(countUnit) && nonNull(stockNumberUnit)) {
                        Integer countUnitSort = countUnit.getSort();
                        Integer stockNumberSort = stockNumberUnit.getSort();
                        long parentUnitStockNumber = 0L;
                        if (countUnit.getSort() < stockNumberUnit.getSort()) {
                            // 由库存单位（较大单位），向统计单位（较小单位）遍历（0, 统计单位, 库存单位）
                            for (int index = stockNumberSort; index > countUnitSort; index--) {
                                parentUnitStockNumber = number / currentStockBatchUnits.get(index).getStepSize();
                            }
                        } else {
                            // 由统计单位（较大单位），向库存单位（较小单位）遍历（0, 库存单位, 统计单位）
                            for (int index = stockNumberSort; index < countUnitSort; index++) {
                                //此处，需要 * 下一级单位的 stepSize
                                // 比如，本单位为箱，有 50 箱，需要得出有多少瓶
                                // （下一级单位瓶，下一级单位进制 stepSize：100，即 100 瓶 = 1 箱）
                                // 总瓶数量 = 50 箱 * 100瓶
                                parentUnitStockNumber = number * currentStockBatchUnits.get(index + 1).getStepSize();
                            }
                        }
                        if (parentUnitStockNumber <= countVal) {
                            return StockStateEnum.SHORTAGE;
                        }
                    }
                }

                // 当库存数量不足 20% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
            } else if (DrugStockRule.MIN_UNIT_PERCENTAGE_20.equals(stateCountRule)) {
                if (number <= totalNumber * 0.2) {
                    return StockStateEnum.SHORTAGE;
                }

                // 当库存数量不足 50% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
            } else if (DrugStockRule.MIN_UNIT_PERCENTAGE_50.equals(stateCountRule)) {
                if (number <= totalNumber * 0.5) {
                    return StockStateEnum.SHORTAGE;
                }

                // 当库存数量不足 80% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
            } else if (DrugStockRule.MIN_UNIT_PERCENTAGE_80.equals(stateCountRule)) {
                if (number <= totalNumber * 0.8) {
                    return StockStateEnum.SHORTAGE;
                }
            }
        }
        return null;
    }

    public DrugExpiryStateEnum computeExpiryState(Integer stockExpiryAlertMonth) {
        if(isNull(expiryState) || EXPIRES.getCode().equals(expiryState) || ABOUT_EXPIRES.getCode().equals(expiryState)) {
            return AppStockService.computeDrugIsExpiry(this, stockExpiryAlertMonth);
        }
        return null;
    }
}