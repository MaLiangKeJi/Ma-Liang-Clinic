package com.bbs.auth.app.login.vo;

import com.bbs.auth.entity.UserCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VO {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 用户名称
     */
    private String name;

    private String token;

    /**
     * 用户公司
     */
    List<UserCompany> userCompanyList;

    /**
     * 是否设置了公司结构
     */
    Boolean isSettingCompanyStructure;

    public VO(Long uid, String name, String token, List<UserCompany> userCompanyList) {
        this.uid = uid;
        this.name = name;
        this.token = token;
        this.userCompanyList = userCompanyList;
    }
}
