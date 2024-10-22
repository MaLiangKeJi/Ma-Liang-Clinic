package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.app.province.Search;
import com.bbs.auth.entity.Province;
import com.bbs.auth.service.ProvinceService;
import com.bbs.auth.mapper.ProvinceMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
* @author ludada
* @description 针对表【province(省市县三级联动（参考https://github.com/uiwjs/province-city-china?tab=readme-ov-file）)】的数据库操作Service实现
* @createDate 2024-09-10 01:13:15
*/
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province>
    implements ProvinceService{

    private static final Integer ROOT_TIER = 3;

    private static final Integer PROVINCE_TIER = 0;

    @Cacheable(cacheNames = "province")
    @Override
    public Set<Search.VO> search(BigInteger id) {
        return lambdaQuery()
                .eq(nonNull(id), Province::getParentId, id)
                .eq(isNull(id), Province::getTier, PROVINCE_TIER)
                .list().stream().map(province -> {
            Search.VO item = new Search.VO(province.getId(), province.getName());
            if (ROOT_TIER.equals(province.getTier())) item.setLeaf(true);
            return item;
        }).collect(Collectors.toSet());
    }
}