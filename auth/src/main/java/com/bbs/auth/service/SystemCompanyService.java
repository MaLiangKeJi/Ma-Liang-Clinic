package com.bbs.auth.service;

import com.bbs.auth.entity.SystemCompany;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ludada
* @description 针对表【back_system_company(后台：公司系统关联表)】的数据库操作Service
* @createDate 2024-07-04 20:17:44
*/
public interface SystemCompanyService extends IService<SystemCompany> {

    SystemCompany search(Long systemId, Long companyId);
}
