package com.bbs.auth.app.company.bind;

import com.bbs.Result;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.entity.Company;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class SearchBindCompany {

    @Resource
    private UserService userService;
    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;
    @Resource
    private CompanyService companyService;

    @GetMapping("/login/bind/company")
    public Result<Company> bingLoginCompany() {
        UserVO loginUser = userService.loginUser();
        Long companyId = bindLoginCompanyCache.get(loginUser.getId());
        return Result.success(companyService.getById(companyId));
    }
}
