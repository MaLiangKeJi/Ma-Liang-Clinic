package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 库存 & 单位中间表
 * @TableName stock_unit
 */
@TableName(value ="stock_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUnit implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 库存编号
     */
    @TableField(value = "batch_id")
    private Long batchId;

    /**
     * 单位编号
     */
    @TableField(value = "unit_id")
    private Integer unitId;

    /**
     * 排序字段
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 1个父级单位对应的本单位数量(如1箱对应8瓶); 默认0代表最大单位
     */
    @TableField(value = "step_size")
    private Long stepSize;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 单位
     */
    @TableField(exist = false)
    private Unit unit;

    /**
     * 单位列表
     */
    @TableField(exist = false)
    private List<Unit> unitList;

    public StockUnit(Long batchId, Integer unitId, Integer sort, Long stepSize) {
        this.batchId = batchId;
        this.unitId = unitId;
        this.sort = sort;
        this.stepSize = stepSize;
    }
}