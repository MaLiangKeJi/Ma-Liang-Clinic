package com.bbs.auth.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VXUser extends User implements Serializable {

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户唯一标识
     */
    private String openid;

    public VXUser(User user, String openid) {
        super(user.getId(), user.getEmail(), user.getName(), user.getPassword(), user.getPhone(), user.getSalt(), user.getState(), user.getStateStr(), user.getExpirationTime(), user.getCreateTime(), user.getUpdateTime(), user.getUserGroupList());
        this.openid = openid;
    }
}
