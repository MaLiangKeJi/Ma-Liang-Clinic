package com.bbs.auth.service;

import com.bbs.auth.app.province.Search;
import com.bbs.auth.entity.Province;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigInteger;
import java.util.Set;

/**
* @author ludada
* @description 针对表【province(省市县三级联动（参考https://github.com/uiwjs/province-city-china?tab=readme-ov-file）)】的数据库操作Service
* @createDate 2024-09-10 01:13:15
*/
public interface ProvinceService extends IService<Province> {

    Set<Search.VO> search(BigInteger id);
}
