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
 * 邀请
 *
 * @TableName invite
 */
@TableName(value = "invite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invite implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "valid_end_time")
    private Date validEndTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Invite(Long userId, String inviteCode) {
        this.userId = userId;
        this.inviteCode = inviteCode;
    }

    public Invite(Long userId, String inviteCode, Date validEndTime) {
        this.userId = userId;
        this.inviteCode = inviteCode;
        this.validEndTime = validEndTime;
    }
}