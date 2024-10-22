package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消毒记录
 * @TableName sterilize_log
 */
@TableName(value ="sterilize_log")
@Data
public class SterilizeLog implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消毒时间
     */
    @TableField(value = "sterilize_time")
    private Date sterilizeTime;

    /**
     * 消毒部位
     */
    @TableField(value = "content")
    private String content;

    /**
     * 消毒方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 消毒剂
     */
    @TableField(value = "disinfector")
    private String disinfector;

    /**
     * 消毒人
     */
    @TableField(value = "executor")
    private String executor;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}