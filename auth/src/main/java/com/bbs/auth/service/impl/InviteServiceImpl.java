package com.bbs.auth.service.impl;

import com.bbs.auth.entity.Invite;
import com.bbs.auth.service.InviteService;
import com.bbs.auth.mapper.InviteMapper;
import com.bbs.auth.util.LoginUser;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
* @author Mafty
* @description 针对表【invite(邀请)】的数据库操作Service实现
* @createDate 2024-08-20 15:28:06
*/
@Service
public class InviteServiceImpl extends MPJBaseServiceImpl<InviteMapper, Invite>
    implements InviteService{

//    @Cacheable(cacheNames = "user::invite")
    @Override
    public Invite search() {
        List<Invite> invList = lambdaQuery().eq(Invite::getUserId, LoginUser.getId()).list();
        if (ObjectUtils.isEmpty(invList))
            return null;
        return invList.get(INTEGER_ZERO);
    }
}