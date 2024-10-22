package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统
 * @TableName system
 */
@TableName(value ="back_system")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class System implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 管理员
     */
    @TableField(value = "admin_id")
    private Long adminId;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 父级系统
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 信息创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 信息修改人
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 状态
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 是否隐藏
     */
    @TableField(value = "is_hide")
    private Integer isHide;

    /**
     * icon
     */
    @TableField(value = "icon_name")
    private String iconName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private User admin;
}