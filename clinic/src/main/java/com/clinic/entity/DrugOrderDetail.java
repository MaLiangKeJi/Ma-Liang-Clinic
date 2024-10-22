package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName drug_order_detail
 */
@TableName(value ="drug_order_detail")
@Data
public class DrugOrderDetail implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 药品订单Id
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单详情
     */
    @TableField(value = "order_detail")
    private String orderDetail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public DrugOrderDetail(Long orderId, String orderDetail) {
        this.orderId = orderId;
        this.orderDetail = orderDetail;
    }
}