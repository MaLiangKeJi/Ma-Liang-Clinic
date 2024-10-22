package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.service.InviteUserService;
import com.bbs.auth.mapper.InviteUserMapper;
import org.springframework.stereotype.Service;

/**
* @author ludada
* @description 针对表【invite_user(邀请用户记录)】的数据库操作Service实现
* @createDate 2024-09-06 19:16:05
*/
@Service
public class InviteUserServiceImpl extends ServiceImpl<InviteUserMapper, InviteUser>
    implements InviteUserService{

}




