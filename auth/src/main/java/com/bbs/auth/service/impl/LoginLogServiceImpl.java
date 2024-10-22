package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.LoginLog;
import com.bbs.auth.service.LoginLogService;
import com.bbs.auth.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService{

}




