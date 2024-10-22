package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 其他收费
 */
@TableName(value ="pay_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayRecord implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 收费记录id
     */
    @TableField(value = "pay_id")
    private Long payId;

    /**
     * 项目名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 费用
     */
    @TableField(value = "fee")
    private BigDecimal fee;

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

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 用户id
     */
    @TableField(value = "creator")
    private Long creator;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public PayRecord(Long payId, BigDecimal price, Long userId) {
        this.payId = payId;
        this.name = "处方";
        this.fee = price;
        this.creator = userId;
    }
}