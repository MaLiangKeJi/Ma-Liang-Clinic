package com.bbs.auth.entity.param;

import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParam extends BaseParam {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 创建时间（起始）
     */
    private Date createStartTime;

    /**
     * 创建时间（结束）
     */
    private Date createEndTime;

    /**
     * 过期时间（起始）
     */
    private Date expirationStartTime;

    /**
     * 过期时间（结束）
     */
    private Date expirationEndTime;

    /**
     * 排序方式（0:即将过期 1:刚刚创建）
     */
    private Integer sortType;
}
