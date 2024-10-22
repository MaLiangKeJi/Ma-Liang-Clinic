package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.SystemCompany;
import com.bbs.auth.service.SystemCompanyService;
import com.bbs.auth.mapper.SystemCompanyMapper;
import org.springframework.stereotype.Service;

/**
* @author ludada
* @description 针对表【back_system_company(后台：公司系统关联表)】的数据库操作Service实现
* @createDate 2024-07-04 20:17:44
*/
@Service
public class SystemCompanyServiceImpl extends ServiceImpl<SystemCompanyMapper, SystemCompany>
    implements SystemCompanyService{

    @Override
    public SystemCompany search(Long systemId, Long companyId) {
        return lambdaQuery()
                .eq(SystemCompany::getSystemId, systemId)
                .eq(SystemCompany::getCompanyId, companyId)
                .one();
    }
}




