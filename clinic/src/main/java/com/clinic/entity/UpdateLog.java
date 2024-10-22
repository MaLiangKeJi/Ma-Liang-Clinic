package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 更新日志
 * @TableName update_log
 */
@TableName(value ="update_log")
@Data
public class UpdateLog implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 版本
     */
    @TableField(value = "version")
    private String version;

    /**
     * 更新标题
     */
    @TableField(value = "update_title")
    private String updateTitle;

    /**
     * 更新简介
     */
    @TableField(value = "update_intro")
    private String updateIntro;

    /**
     * 更新内容
     */
    @TableField(value = "update_text")
    private String updateText;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}