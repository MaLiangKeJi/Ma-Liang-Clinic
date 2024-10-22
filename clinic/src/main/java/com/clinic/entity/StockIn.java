package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.github.yulichang.annotation.EntityMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * 库存：入库
 * @TableName stock_in
 */
@TableName(value ="stock_in")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class StockIn implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入库编号
     */
    @TableField(value = "no")
    private String no;

    /**
     * 总价
     */
    @TableField(value = "total_cost")
    private BigDecimal totalCost;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    @EntityMapping(thisField = StockIn.Fields.id, joinField = StockInDrug.Fields.stockInId)
    private StockInDrug stockInDrugs;

    public StockIn(Long id, String no, BigDecimal totalCost, Long userId, String remark, Date createTime) {
        this.id = id;
        this.no = no;
        this.totalCost = totalCost;
        this.userId = userId;
        this.remark = remark;
        this.createTime = createTime;
    }
}