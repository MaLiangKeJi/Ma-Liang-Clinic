package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 路由：元数据
 * @TableName system_router
 */
@TableName(value ="back_system_router")
@Data
public class SystemRouter implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统ID
     */
    @TableField(value = "system_id")
    private Long systemId;

    /**
     * 编码（驼峰）
     */
    @TableField(value = "code")
    private String code;

    /**
     * 路由路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @TableField(value = "component_path")
    private String componentPath;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 类型（页面、弹窗、组件）
     */
    @TableField(value = "type")
    private Integer type;

    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "weight")
    private Long weight;

    @TableField(value = "icon_name")
    private String iconName;

    /**
     * 是否需要管理员权限
     */
    @TableField(value = "is_admin")
    private Integer isAdmin;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private System system;

    @TableField(exist = false)
    private SystemRouter parent;
}