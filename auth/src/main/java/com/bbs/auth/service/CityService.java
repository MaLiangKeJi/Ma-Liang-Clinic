package com.bbs.auth.service;

import com.bbs.auth.entity.City;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 路晨霖
* @description 针对表【city(三级联动)】的数据库操作Service
* @createDate 2024-04-11 15:58:44
*/
public interface CityService extends IService<City> {

    Page<City> search(Integer type, String name, Integer current, Integer size);

    Page<City> search(Long pid, Integer current, Integer size);
}
