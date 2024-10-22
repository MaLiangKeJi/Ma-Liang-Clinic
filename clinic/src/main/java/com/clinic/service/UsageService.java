package com.clinic.service;

import com.clinic.entity.Usage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 路晨霖
* @description 针对表【usage(用法)】的数据库操作Service
* @createDate 2023-12-14 13:21:24
*/
public interface UsageService extends IService<Usage> {


    List<Usage> search(String name);
}
