package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 诊断证明
 * @TableName diagnosis_proof
 */
@TableName(value ="diagnosis_proof")
@Data
public class DiagnosisProof implements Serializable {
    /**
     * 编号
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

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 就诊时间
     */
    @TableField(value = "visit_date")
    private Date visitDate;

    /**
     * 住址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 诊断
     */
    @TableField(value = "diagnosis")
    private String diagnosis;

    /**
     * 处理
     */
    @TableField(value = "deal_with")
    private String dealWith;

    /**
     * 创建者（用户）编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}