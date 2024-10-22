package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消杀记录
 * @TableName disinfection_log
 */
@TableName(value ="disinfection_log")
@Data
public class DisinfectionLog implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消杀日期
     */
    @TableField(value = "disinfection_time")
    private Date disinfectionTime;

    /**
     * 消杀内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 消杀药品、器械
     */
    @TableField(value = "items")
    private String items;

    /**
     * 消杀时间范围
     */
    @TableField(value = "time_range")
    private String timeRange;

    /**
     * 消杀地点
     */
    @TableField(value = "spot")
    private String spot;

    /**
     * 消杀人
     */
    @TableField(value = "executor")
    private String executor;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}