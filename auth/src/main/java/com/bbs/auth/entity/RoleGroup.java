package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色 & 组关联
 * @TableName role_group
 */
@TableName(value ="role_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleGroup implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色编号
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 组编号
     */
    @TableField(value = "group_id")
    private Long groupId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}