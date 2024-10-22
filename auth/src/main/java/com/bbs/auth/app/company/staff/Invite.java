package com.bbs.auth.app.company.staff;

import com.bbs.Result;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController("inviteStaff")
@RequestMapping
public class Invite {

    @Resource
    private CompanyService companyService;
    @Resource
    private UserService userService;

    @PutMapping("/company/staff/invite")
    public Result<Boolean> invite(@Valid @RequestBody Param param) {
        Long companyId = userService.loginUser().getCompanyId();
        companyService.join(param.userID, companyId, param.getStructureID(), param.getIsAdmin());
        return Result.success();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {

        @NotNull
        private Long structureID;

        @NotNull
        private Long userID;

        private Boolean isAdmin;
    }
}
