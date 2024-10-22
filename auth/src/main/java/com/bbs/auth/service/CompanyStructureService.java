package com.bbs.auth.service;

import com.bbs.auth.entity.CompanyStructure;
import com.github.yulichang.base.MPJBaseService;

/**
* @author 路晨霖
* @description 针对表【company_structure(公司架构)】的数据库操作Service
* @createDate 2024-04-29 22:47:45
*/
public interface CompanyStructureService extends MPJBaseService<CompanyStructure> {

    CompanyStructure search(Long id);

    CompanyStructure search(Long companyId, String structureName);
}
