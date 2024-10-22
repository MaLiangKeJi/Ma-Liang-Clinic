package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 受邀奖励领取
 * @TableName invitation_reward
 */
@TableName(value ="invitation_reward")
@Data
public class InvitationReward implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 受邀用户
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

    /**
     * 领取状态
     */
    @TableField(value = "state")
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public InvitationReward(Long userId, String inviteCode, Integer state) {
        this.userId = userId;
        this.inviteCode = inviteCode;
        this.state = state;
    }
}