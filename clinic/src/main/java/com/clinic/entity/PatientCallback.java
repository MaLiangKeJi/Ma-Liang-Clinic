package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 患者回访
 * @TableName patient_callback
 */
@TableName(value ="patient_callback")
@Data
public class PatientCallback implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（诊所ID）
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 患者ID
     */
    @TableField(value = "patient_id")
    private Long patientId;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 类 & 方法名
     */
    @TableField(value = "class_method_name")
    private String classMethodName;

    /**
     * 参数
     */
    @TableField(value = "params")
    private String params;

    /**
     * 需要回访的日期
     */
    @TableField(value = "callback_date")
    private Date callbackDate;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public PatientCallback(Long userId, Long patientId, Integer type, String classMethodName, String params, Date callbackDate) {
        this.userId = userId;
        this.patientId = patientId;
        this.type = type;
        this.classMethodName = classMethodName;
        this.params = params;
        this.callbackDate = callbackDate;
    }
}