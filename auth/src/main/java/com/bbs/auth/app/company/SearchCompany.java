package com.bbs.auth.app.company;

import com.bbs.Result;
import com.bbs.auth.entity.Company;
import com.bbs.auth.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping
public class SearchCompany {

    @Resource
    private CompanyService companyService;

    @GetMapping("/company")
    public Result<List<Company>> search(
            @RequestParam String companyName
    ) {
        return Result.success(companyService.lambdaQuery()
                .like(Company::getName, companyName)
                .list());
    }
}
