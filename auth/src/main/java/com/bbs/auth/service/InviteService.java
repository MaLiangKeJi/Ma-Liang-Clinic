package com.bbs.auth.service;

import com.bbs.auth.entity.Invite;
import com.github.yulichang.base.MPJBaseService;

/**
 * @author Mafty
 * @description 针对表【invite(邀请)】的数据库操作Service
 * @createDate 2024-08-20 15:28:06
 */
public interface InviteService extends MPJBaseService<Invite> {

    Invite search();
}