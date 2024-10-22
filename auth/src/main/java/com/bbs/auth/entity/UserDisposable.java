package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户相关的一次性操作（如一次性的引导弹窗、广告推送等等）
 * @TableName user_disposable
 */
@TableName(value ="user_disposable")
@Data
public class UserDisposable implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public UserDisposable(Long userId, String code) {
        this.userId = userId;
        this.code = code;
    }
}