package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统购买记录
 * @TableName renew_log
 */
@TableName(value ="renew_log")
@Data
public class RenewLog implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 购买系统id
     */
    @TableField(value = "back_system_id")
    private Long backSystemId;

    /**
     * 支付订单id
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 套餐：1月2季3年
     */
    @TableField(value = "package_type")
    private Integer packageType;

    /**
     * 支付状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 购买人id
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private String createTimeStr;

    /**
     * 删除状态
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    @TableField(value = "money")
    private BigDecimal money;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Integer payNumber;
}