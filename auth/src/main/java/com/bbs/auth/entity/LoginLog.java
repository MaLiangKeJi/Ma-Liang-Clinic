package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bbs.auth.app.login.param.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录日志
 * @TableName login_log
 */
@TableName(value ="login_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录结果（0正常/1失败）
     */
    @TableField(value = "result")
    private Integer result;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 输入验证码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 登录类型
     */
    @TableField(value = "login_type")
    private String loginType;

    /**
     * 服务端保存的手机验证码
     */
    @TableField(value = "server_save_phone_code")
    private Integer serverSavePhoneCode;

    /**
     * 登录成功生成的 Token
     */
    @TableField(value = "new_token")
    private String newToken;

    /**
     * 失败原因
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private String loginTime;

    @TableField(value = "user_id")
    private Long userId;


    public LoginLog(Integer result, Param param, String loginTime) {
        this.result = result;
        this.phone = param.getPhone();
        this.code = param.getCode();
        this.loginType = param.getLoginType();
        this.loginTime = loginTime;
    }


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}