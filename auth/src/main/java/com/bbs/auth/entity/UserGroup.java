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
 * 用户 & 组关联表
 * @TableName user_group
 */
@TableName(value ="user_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 组编号
     */
    @TableField(value = "group_id")
    private Long groupId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public UserGroup(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @TableField(exist = false)
    private Group group;
}