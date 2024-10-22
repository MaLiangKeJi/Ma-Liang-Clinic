package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 零售：消费记录
 * @TableName retail_record
 */
@TableName(value ="retail_record")
@Data
public class RetailRecord implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 出生年月
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private Long phone;

    /**
     * 身份证
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 联系人
     */
    @TableField(value = "contact")
    private String contact;

    /**
     * 联系人电话
     */
    @TableField(value = "contact_phone")
    private Long contactPhone;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 总价
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 创建日期
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 顾客ID
     */
    @TableField(value = "patient_id")
    private Long patientId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<RetailDrugRecord> retailDrugRecords;
}