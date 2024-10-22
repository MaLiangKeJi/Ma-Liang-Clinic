package com.bbs.auth.service;

import com.bbs.auth.entity.System;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 路晨霖
* @description 针对表【system(系统)】的数据库操作Service
* @createDate 2024-07-02 19:01:34
*/
public interface SystemService extends IService<System> {


    System searchBySystemCode(String code);

}
