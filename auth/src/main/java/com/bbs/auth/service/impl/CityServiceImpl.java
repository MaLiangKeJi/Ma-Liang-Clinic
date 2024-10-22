package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.City;
import com.bbs.auth.service.CityService;
import com.bbs.auth.mapper.CityMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

/**
* @author 路晨霖
* @description 针对表【city(三级联动)】的数据库操作Service实现
* @createDate 2024-04-11 15:58:44
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService{

    @Cacheable("city")
    @Override
    public Page<City> search(Integer type, String name, Integer current, Integer size) {
        return baseMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<City>()
                .eq(nonNull(type), City::getType, type)
                .like(StringUtils.isNotBlank(name), City::getName, name));
    }

    @Override
    public Page<City> search(Long pid, Integer current, Integer size) {
        return lambdaQuery().eq(City::getPid, pid).page(new Page<>(current, size));
    }
}




