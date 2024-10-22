package com.bbs.auth.app.login.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {

    /**
     * 手机号
     */
    @NotBlank
    @Length(max = 11, min = 11, message = "手机号格式错误")
    private String phone;

    /**
     * 验证码
     */
    private String code;

    private String password;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 是否查询用户公司信息
     */
    private Boolean searchCompany;

    /**
     * 是否检查公司结构（增加【是否设置公司结构】的查询结果）
     * 注：需要 searchCompany = true
     */
    private Boolean checkCompanyStructure;

    /**
     * 是否查询用户公司信息
     */
    private Boolean is;

    private String openId;

    /**
     * 过期时间（单位默认天）
     */
    private Integer expireNumber;

    private String inviteCode;
}
