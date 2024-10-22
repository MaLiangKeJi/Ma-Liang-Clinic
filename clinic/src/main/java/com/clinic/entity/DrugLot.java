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
 * 药品批次表
 * @TableName drug_batch
 */
@TableName(value ="drug_lot")
@Data
public class DrugLot implements Serializable {

    /**
     *  用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 批次编号
     */
    @TableField(value = "lot_no")
    private String lotNo;

    /**
     * 0入库/1出库
     */
    @TableField(value = "lot_type")
    private Integer lotType;

    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;



    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除状态：0未删除  1已删除
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}