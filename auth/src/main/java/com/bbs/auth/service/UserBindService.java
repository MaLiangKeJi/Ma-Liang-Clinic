package com.bbs.auth.service;

import com.bbs.auth.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 路晨霖
* @description 针对表【user_bind(用户绑定表)】的数据库操作Service
* @createDate 2024-03-19 21:48:13
*/
public interface UserBindService extends IService<UserBind> {

    UserBind searchUserBind(String openID);
}
