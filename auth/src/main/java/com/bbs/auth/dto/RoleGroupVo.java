package com.bbs.auth.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色 & 组关联
 * @TableName role_group
 */
@TableName(value ="role_group")
@Data
public class RoleGroupVo implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 组名称
     */
    @TableField(value = "name")
    private String name;


    /**
     * 角色编号
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField(value = "roleName")
    private String roleName;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}