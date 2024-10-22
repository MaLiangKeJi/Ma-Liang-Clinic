package com.bbs.auth.app.back.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.Company;
import com.bbs.auth.entity.SystemCompany;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.SystemCompanyService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.BaseParam;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping
@RestController("backSearchCompany")
@Slf4j
public class SearchCompany {

    @Resource
    private UserService userService;

    @Resource
    private CompanyService companyService;

    @GetMapping("/back/company")
    public Result<Page<Company>> search(BaseParam param) {
        User user = userService.loginEntityUser();
        Preconditions.checkArgument(user.isSupperAdmin(), "当前账号无权限");
        return Result.success(companyService.selectJoinListPage(param.toPage(), Company.class, new MPJLambdaWrapper<Company>()
                .selectAll(Company.class)
                .selectCount(Company::getId, Company::getStaffNum)
                .leftJoin(User.class, User::getId, Company::getCreateBy, ext -> ext.selectAssociation(User.class, Company::getCreateUser))
                .leftJoin(User.class, User::getId, Company::getUpdateBy, ext -> ext.selectAssociation(User.class, Company::getUpdateUser))
                .leftJoin(User.class, User::getId, Company::getAdminId, ext -> ext.selectAssociation(User.class, Company::getAdmin))
                .leftJoin(UserCompany.class, UserCompany::getCompanyId, Company::getId)
                .groupBy(Company::getId)
        ));
    }
}
