package com.bbs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String clinicName;

    private String token;

    private Long failureTokenTime;

    private Integer sex;

    private String idCard;

    private Long companyId;

    /**
     * 图片 URL 地址
     */
    private String avatar;

    public UserVO(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
