package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 病人信息登记表
 * @TableName patient
 */
@TableName(value ="patient")
@Data
public class Patient implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    @TableField(exist = false)
    private String sexStr;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    @TableField(exist = false)
    private String ageStr;

    /**
     * 出生年月
     */
    @TableField(value = "birth_date")
    private Date birthDate;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private Long phone;

    /**
     * 证件号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 住址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 民族
     */
    @TableField(value = "nation")
    private Integer nation;

    /**
     * 婚姻
     */
    @TableField(value = "marriage")
    private Integer marriage;

    /**
     * 职业
     */
    @TableField(value = "job")
    private String job;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 最近就诊时间
     */
    @TableField(value = "last_visit")
    private String lastVisit;

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
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 初复诊（0初诊， 1复诊）
     */
    @TableField(value = "is_first")
    private Integer isFirst;

    /**
     * 关联微信编号
     */
    @TableField(value = "open_id")
    private String openId;


    /**
     *病例数量
     */
    @TableField(exist = false)
    private Integer dossierNum;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}