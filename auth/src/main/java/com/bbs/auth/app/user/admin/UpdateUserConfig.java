package com.bbs.auth.app.user.admin;

import com.bbs.auth.entity.UserGroup;
import com.bbs.auth.service.UserGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.Result;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.entity.User;
import com.bbs.auth.mapper.UserMapper;
import com.bbs.auth.service.PermissionService;
import com.bbs.auth.service.UserService;
import com.bbs.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Date;

import static com.bbs.Result.failed;
import static com.bbs.Result.success;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@RestController
@RequestMapping
public class UpdateUserConfig extends ServiceImpl<UserMapper, User> {

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserService service;

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Resource
    private TransactionDefinition transactionDefinition;

    @Resource
    private UserCache cache;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AdminUpdateUserParam {
        @NotNull
        private Long id;
        private String email;
        private String password;

        private Date expirationTime;

        private Long groupId;
    }

    /**
     * 修改（由管理员执行）
     * @param param Info ID
     * @return 重置结果
     */
    @PostMapping("/user/admin")
    public Result<Boolean> adminResetUserPassword(@RequestBody @Valid AdminUpdateUserParam param) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            checkUserIsAdmin();

            User user = cache.search(param.id);

            checkUserExists(user);

            user.setExpirationTime(param.getExpirationTime());
            update(user, param.email, param.password);

            saveOrUpdateUserGroup(param);

            transactionManager.commit(transaction);
            return success(true);

        } catch (BusinessException e) {
            e.printStackTrace();
            transactionManager.rollback(transaction);
            return failed(e.getCode(), e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            transactionManager.rollback(transaction);
            return failed(e.getMessage());
        }
    }

    private void saveOrUpdateUserGroup(AdminUpdateUserParam param) throws BusinessException {
        UserGroup group = userGroupService.lambdaQuery()
                .eq(UserGroup::getUserId, param.id).one();
        if(nonNull(group)) {
            group.setGroupId(param.groupId);
            if(!userGroupService.updateById(group)) throw new BusinessException("修改用户组权限失败！");
        } else {
            if(!userGroupService.save(new UserGroup(param.id, param.groupId))) throw new BusinessException("保存用户组权限失败！");
        }
    }



    private void checkUserIsAdmin() throws BusinessException {
        Boolean loginUserIsAdmin = permissionService.loginUserIsAdmin();
        if(!(nonNull(loginUserIsAdmin) && loginUserIsAdmin)) throw new BusinessException(400, "您无权限执行此操作！");
    }

    private void checkUserExists(User user) throws BusinessException  {
        if(isNull(user)) throw new BusinessException(400, "对应 ID 用户不存在！");
    }

    private void update(User user, String email, String password) throws BusinessException, InterruptedException {
        String newPassword = service.encryptPassword(password, user.getSalt());
        if(isNotBlank(email)) user.setEmail(email);
        if(isNotBlank(password)) user.setPassword(newPassword);
        cache.updateByID(user);
    }
}
