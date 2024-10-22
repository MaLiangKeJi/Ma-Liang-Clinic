package com.bbs.auth.service;

import com.bbs.auth.entity.UserGroup;

import java.util.List;

public interface PermissionService {

    List<UserGroup> query(Long uid);

    Boolean permissionIsAdmin(List<UserGroup> userGroups);

    /**
     * 用户是否是管理员
     * @param uid 用户 ID（主键）
     * @return 用户是否是管理员
     */
    Boolean userIsAdmin(Long uid);

    /**
     * 登录用户是否是管理员
     * @return 登录用户是否是管理员 or 用户登录信息校验失败（未登录 or 登录信息异常）
     */
    Boolean loginUserIsAdmin();
}
