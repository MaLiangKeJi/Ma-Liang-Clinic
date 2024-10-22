package com.bbs.auth.app.company;

import com.bbs.Result;
import com.bbs.auth.converter.CompanyConverter;
import com.bbs.auth.entity.Company;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.UserCompanyService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping
public class RegisterOrJoinCompany {

    @Resource
    private CompanyService service;
    @Resource
    private CompanyConverter converter;
    @Resource
    private UserCompanyService userCompanyService;
    @Resource
    private UserService userService;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private DataSourceTransactionManager transactionManager;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {
        /**
         * 名称
         */
        @NotBlank(message = "公司名称不能为空")
        private String name;

        /**
         * 所属地区三级联动 ID
         */
        private Long attributionID;

        /**
         * 注册地址
         */
        private String registeredAddress;

        /**
         * 行业（国标码）
         */
        private Long normIndustryId;

        /**
         * 当前用户在该公司的职位
         */
        private String position;
    }

    @PutMapping("/company")
    public Result<List<UserCompany>> registerOrJoin(@RequestBody Param param) {
        UserVO loginUser = userService.loginUser();
        Long loginUserID = loginUser.getId();
        Long companyId = loginUser.getCompanyId();
        // 通过判断是否传入公司 ID，决定是新增公司还是加入公司
        if(nonNull(companyId)) {
            userCompanyService.save(new UserCompany()
                    .setUserId(loginUserID)
                    .setCompanyId(companyId)
                    .setPositionName(param.getPosition())
                    .setCreateBy(loginUserID)
            );
        } else {
            Company company = converter.toEntity(param);
            Preconditions.checkArgument(service.notExists(company), "注册信息对应公司已存在，无法注册");
            company.setCreateBy(loginUserID);
            company.setAdminId(loginUserID);
            TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
            try {
                service.save(company);
                userCompanyService.save(new UserCompany()
                        .setUserId(loginUserID)
                        .setCompanyId(company.getId())
                        .setPositionName(param.getPosition())
                        .setCreateBy(loginUserID)
                );
                transactionManager.commit(transaction);
            } catch (Exception e) {
                assert transactionManager != null;
                transactionManager.rollback(transaction);
                throw new RuntimeException(e);
            }
        }
        return Result.success(service.searchCompany(loginUserID));
    }
}
