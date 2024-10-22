package com.bbs.auth.service.impl;

import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserBind;
import com.bbs.auth.service.UserBindService;
import com.bbs.auth.mapper.UserBindMapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【user_bind(用户绑定表)】的数据库操作Service实现
* @createDate 2024-03-19 21:48:13
*/
@Service
public class UserBindServiceImpl extends MPJBaseServiceImpl<UserBindMapper, UserBind>
    implements UserBindService{

    @Override
    public UserBind searchUserBind(String openID) {
        return selectJoinOne(UserBind.class, new MPJLambdaWrapper<UserBind>()
                .selectAll(UserBind.class)
                .selectAssociation(User.class, UserBind::getUser)
                .leftJoin(User.class, User::getId, UserBind::getUserId)
                .eq(UserBind::getState, NumberUtils.INTEGER_ZERO)
                .eq(UserBind::getOpenId, openID));
    }
}




