package com.bbs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.auth.app.user.Search;
import com.bbs.auth.entity.Fan;

import java.util.List;

/**
 * 关注粉丝群
 */
public interface FanService extends IService<Fan> {

    Boolean create(Fan fan);

    void delFollow(Long uid, Long followUserID);

    List<Fan> getFollow(Long userId);

    /**
     * 查询 userId 的粉丝
     * @param userId 用户ID
     * @return 关注了 userID 的用户
     */
    List<Fan> getFan(Long userId);

    Long count(Long userId);

    /**
     * 当前登录用户，是否关注了 followUserID 用户
     * @param followUserID 目标用户
     * @return 是否关注了目标用户
     */
    Boolean isFollow(Long followUserID);

    void fillFollowStatus(List<Long> ids, List<Search.VO> fillObjs);
}
