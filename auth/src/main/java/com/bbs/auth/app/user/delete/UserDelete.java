package com.bbs.auth.app.user.delete;

import com.bbs.Result;
import com.bbs.auth.cache.UserPermissionCache;
import com.bbs.auth.cache.code.PhoneCodeCache;
import com.bbs.auth.cache.user.PhoneCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.entity.Invite;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.*;
import com.bbs.auth.util.ApiPermissionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.bbs.auth.util.ApiPermissionManagement.checkApiPermissionManagementCode;

@RestController
@RequestMapping
public class UserDelete {

    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;
    @Resource
    private UserCache userCache;
    @Resource
    private UserPermissionCache userPermissionCache;
    @Resource
    private PhoneCache phoneCache;
    @Resource
    private PhoneCodeCache phoneCodeCache;
    @Resource
    private CompanyService companyService;
    @Resource
    private InviteService inviteService;
    @Resource
    private InviteUserService inviteUserService;
    /**
     * 注销用户
     */
    @Transactional
    @DeleteMapping("/user")
    public Result<Boolean> delete(@RequestBody ApiPermissionManagement.Param param) {

        checkApiPermissionManagementCode(param);


        delete(userService.loginUser().getId());

        return Result.success();
    }

    public void delete(Long loginUserId) {
        // 查询登录用户
        User user = userService.search(loginUserId);
        String phone = user.getPhone().toString();

        // 删除缓存
        removeCache(loginUserId, phone);

        // 删除用户
        userService.removeById(loginUserId);
        // 删除公司
        companyService.deleteAllCompanyByUserId(loginUserId);

        companyService.deleteAllCompanyByUserId(loginUserId);
        inviteService.lambdaUpdate().eq(Invite::getUserId, loginUserId).remove();
        inviteUserService.lambdaUpdate().eq(InviteUser::getInitiatorUserId, loginUserId).remove();
    }

    private void removeCache(Long loginUserId, String phone) {
        tokenService.clearLoginFlag(loginUserId);
        userCache.remove(loginUserId);
        userPermissionCache.remove(loginUserId);
        phoneCache.remove(phone);
        phoneCodeCache.delCode(phone);
    }
}
