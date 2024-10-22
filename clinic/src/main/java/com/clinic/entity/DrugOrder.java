package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName drug_order
 */
@TableName(value ="drug_order")
@Data
public class DrugOrder implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 医生姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 收货地址
     */
    @TableField(value = "receiver_address")
    private String receiverAddress;

    /**
     * 订单状态
     */
    @TableField(value = "status")
    private OrderStatus status;

    /**
     * 医生（用户）Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 送货方式
     */
    @TableField(value = "receiver_method")
    private Integer receiverMethod;

    /**
     * 总价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 药代id
     */
    @TableField(value = "agents_id")
    private Long agentsId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public DrugOrder(String name, String receiverAddress, OrderStatus status, Long userId) {
        this.name = name;
        this.receiverAddress = receiverAddress;
        this.status = status;
        this.userId = userId;
    }
}