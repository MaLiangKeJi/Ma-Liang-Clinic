package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 粉丝和关注表
 * @TableName fan
 */
@TableName(value ="fan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fan implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 关注账号id
     */
    @TableField(value = "follow_user_id")
    private Long followUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 删除状态：0未删除  1已删除
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Fan(Long userId, Long followUserId) {
        this.userId = userId;
        this.followUserId = followUserId;
    }
}