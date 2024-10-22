package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.event.Level;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * @TableName log_operation
 */
@TableName(value ="log_operation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 记录时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private String createYMD;

    @TableField(exist = false)
    private Boolean isCurrentYear;

    @TableField(exist = false)
    private String createMD;

    @TableField(exist = false)
    private String createHMS;

    @TableField(exist = false)
    private String createHMSTwelveHourlySystem;

    @TableField(exist = false)
    private Boolean isCurrentDay;

    /**
     * 业务编码
     */
    @TableField(value = "service_code")
    private Integer serviceCode;

    /**
     * 业务
     */
    @TableField(value = "service")
    private String service;

    /**
     * 操作简介
     */
    @TableField(value = "about")
    private String about;

    /**
     * 位置
     */
    @TableField(value = "location")
    private String location;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 操作内容
     */
    @TableField(value = "operation")
    private String operation;

    /**
     * 级别
     * @see org.slf4j.event.Level
     * @see org.slf4j.event.EventConstants
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 病人 or 顾客 ID
     */
    @TableField(value = "patient_id")
    private Long patientId;

    /**
     * 参数
     */
    @TableField(value = "param")
    private String param;

    @TableField(exist = false)
    private Patient patient;

    public OperationLog(String location, Integer serviceCode, String service, String about, String operation, Level level, Long patientId, String param) {
        this.serviceCode = serviceCode;
        this.createTime = new Date();
        this.location = location;
        this.service = service;
        this.about = about;
        this.userId = LoginUser.getId();
        this.operation = operation;
        this.level = level.toInt();
        this.patientId = patientId;
        this.param = param;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}