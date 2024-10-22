package com.bbs.auth.service.impl;

import cn.hutool.json.JSONUtil;
import com.bbs.auth.entity.CompanyStructure;
import com.bbs.auth.mapper.CompanyStructureMapper;
import com.bbs.auth.service.CompanyStructureService;
import com.bbs.auth.util.RedisUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.bbs.auth.enums.RedisKeys.COMPANY_STRUCTURE;

/**
* @author 路晨霖
* @description 针对表【company_structure(公司架构)】的数据库操作Service实现
* @createDate 2024-04-29 22:47:45
*/
@Service
public class CompanyStructureServiceImpl extends MPJBaseServiceImpl<CompanyStructureMapper, CompanyStructure>
    implements CompanyStructureService{

    @Resource
    private RedisUtil redisUtil;

    private String cacheKey(Long id) {
        return COMPANY_STRUCTURE.key(id);
    }

    @Override
    public CompanyStructure search(Long id) {
        String cacheKey = cacheKey(id);
        String str = redisUtil.get(cacheKey);
        CompanyStructure companyStructure;
        if(StringUtils.isNotBlank(str)) {
            companyStructure = JSONUtil.toBean(str, CompanyStructure.class);
        } else {
            companyStructure = getById(id);
            redisUtil.set(cacheKey, JSONUtil.toJsonPrettyStr(companyStructure));
        }
        return companyStructure;
    }


    @Override
    public CompanyStructure search(Long companyId, String structureName) {
          return lambdaQuery().eq(CompanyStructure::getCompanyId, companyId).eq(StringUtils.isNotBlank(structureName),CompanyStructure::getName, structureName).one();
    }
}




