package com.bbs.auth.app.company.bind;

import com.bbs.Result;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping
public class BingLoginCompany {

    @Resource
    private UserService userService;
    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        @NotNull
        private Long companyId;
    }

    @PostMapping("/login/bind/company")
    public Result<Boolean> bingLoginCompany(@RequestBody Param param) {
        UserVO loginUser = userService.loginUser();
        bindLoginCompanyCache.bind(loginUser.getId(), param.getCompanyId());
        return Result.success();
    }
}
