package com.bbs.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.auth.entity.Company;
import com.bbs.auth.entity.CompanyStructure;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserCompany;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;
import java.util.Set;

/**
* @author 路晨霖
* @description 针对表【company(公司)】的数据库操作Service
* @createDate 2024-04-28 11:36:25
*/
public interface CompanyService extends MPJBaseService<Company> {

    Boolean exists(Company company);

    Boolean notExists(Company company);

    List<CompanyStructure> searchStructure(Long companyID);

    List<CompanyStructure> searchStructure();

    List<CompanyStructure> searchStructure(Long companyID, List<String> name);

    CompanyStructure searchStructure(Long companyID, String name);

    Company searchCompanyStaff(Long companyID, Page<UserCompany> page);

    List<UserCompany> searchCompany(Long uid);

    /**
     * 是否设置过公司结构
     * @param companyID 公司 ID
     * @return 是否设置过公司结构
     */
    Boolean searchIsSetCompanyStructure(Long companyID);

    void createCompanyStructure(CompanyStructure companyStructure);

    /**
     * 将用户加入公司
     * @param userID 用户ID
     * @param companyID 公司ID
     * @param structureID 部门ID
     * @param isAdmin 是否为管理员
     */
    void join(Long userID, Long companyID, Long structureID, Boolean isAdmin);

    CompanyStructure searchUserCompanyStructure(Long uid, Long companyID);

    List<User> searchStructureStaff(Set<Long> structureIds);

    List<CompanyStructure> searchStructure(Set<Long> structureIds);

    void deleteAllCompanyByUserId(Long userId);
}
