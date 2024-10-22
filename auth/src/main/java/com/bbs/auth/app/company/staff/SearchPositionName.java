package com.bbs.auth.app.company.staff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbs.Result;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.service.UserCompanyService;
import com.bbs.auth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping
public class SearchPositionName {

    @Resource
    private UserCompanyService userCompanyService;
    @Resource
    private UserService userService;

    @GetMapping("/staff/position")
    public Result<List<String>> search(
            @RequestParam(required = false) String positionName) {
        return Result.success(userCompanyService.listObjs(new LambdaQueryWrapper<UserCompany>()
                .select(UserCompany::getPositionName)
                .eq(UserCompany::getCompanyId, userService.loginUser().getCompanyId())
                .like(StringUtils.isNotBlank(positionName), UserCompany::getPositionName, positionName)
                .isNotNull(UserCompany::getPositionName)
                .groupBy(UserCompany::getPositionName)
        ));
    }
}
