package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存：出库
 * @TableName stock_out
 */
@TableName(value ="stock_out")
@Data
public class StockOut implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 处方编号
     */
    @TableField(value = "dossier_id")
    private Long dossierId;

    /**
     * 库存编号
     */
    @TableField(value = "stock_id")
    private Long stockId;

    /**
     * 单位
     */
    @TableField(value = "unit_id")
    private Long unitId;

    /**
     * 成本价格
     */
    @TableField(value = "cost")
    private Integer cost;

    /**
     * 单价
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 利润
     */
    @TableField(value = "profit")
    private Integer profit;

    /**
     * 总数量
     */
    @TableField(value = "number")
    private Long number;

    /**
     * 总成本
     */
    @TableField(value = "total_cost")
    private Integer totalCost;

    /**
     * 总利润
     */
    @TableField(value = "total_profit")
    private Integer totalProfit;

    /**
     * 总价
     */
    @TableField(value = "total_price")
    private Integer totalPrice;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}