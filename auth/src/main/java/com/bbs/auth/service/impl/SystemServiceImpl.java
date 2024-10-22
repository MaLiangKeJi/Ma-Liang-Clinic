package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.System;
import com.bbs.auth.service.SystemService;
import com.bbs.auth.mapper.SystemMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【system(系统)】的数据库操作Service实现
* @createDate 2024-07-02 19:01:34
*/
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System>
    implements SystemService{

    @Cacheable(cacheNames = "system::code")
    @Override
    public System searchBySystemCode(String code) {
        return lambdaQuery()
                .eq(System::getCode, code)
                .one();
    }
}




