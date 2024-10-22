package com.bbs.auth.event.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow implements Serializable {

    /**
     * 关注者账号id
     */
    private Long userId;

    /**
     * 被关注者账号id
     */
    private Long followUserId;

    /**
     * 创建时间
     */
    private Date createTime;
}
