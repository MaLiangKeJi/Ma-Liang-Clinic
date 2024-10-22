package com.bbs.api.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long id;
    /**
     * 头像 URL 地址
     */
    private String avatar;

    private String name;

    private String email;

    private Long phone;

    private String token;

    private Long failureTokenTime;

    /**
     * 是否关注了当前登录用户
     */
    private Boolean isFollow;


    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 工号
     */
    private String jobCard;

    /**
     * 公司 ID
     */
    private Long companyId;

    /**
     * 部门名称（公司结构名称）
     */
    private String structureName;

    public User(String jobId, String name, Long phone, String idCard, String groupName) {
        this.jobCard = jobId;
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
        this.structureName = groupName;
    }


}
