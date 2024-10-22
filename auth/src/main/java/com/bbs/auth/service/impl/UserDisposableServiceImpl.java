package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.UserDisposable;
import com.bbs.auth.service.UserDisposableService;
import com.bbs.auth.mapper.UserDisposableMapper;
import org.springframework.stereotype.Service;

/**
* @author ludada
* @description 针对表【user_disposable(用户相关的一次性操作（如一次性的引导弹窗、广告推送等等）)】的数据库操作Service实现
* @createDate 2024-09-10 23:46:16
*/
@Service
public class UserDisposableServiceImpl extends ServiceImpl<UserDisposableMapper, UserDisposable>
    implements UserDisposableService{

}




