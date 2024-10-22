package com.bbs.auth.app.reset;

import com.bbs.auth.service.TokenService;
import com.bbs.auth.cache.code.PhoneCodeCache;
import com.bbs.auth.cache.user.PhoneCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.service.UserService;
import com.bbs.auth.util.RedisUtil;
import com.bbs.auth.entity.User;
import com.bbs.Result;
import com.bbs.exception.BusinessException;
import com.bbs.util.PhoneUtil;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.bbs.Result.failed;
import static com.bbs.auth.cache.user.UserCache.notRegistered;
import static com.bbs.auth.enums.RedisKeys.*;
import static com.bbs.Result.success;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RestController
@RequestMapping
public class ChangePhone {
    @Resource
    private RedisUtil.Redisson redissonUtil;

    @Resource
    private RedissonClient redisson;

    @Lazy
    @Resource
    private UserDao db;

    @Lazy
    @Resource
    private PhoneCodeCache codeCache;

    @Lazy
    @Resource
    private PhoneCache phoneCache;

    @Resource
    private UserService service;

    @Resource
    private UserCache userCache;

    @Resource
    private TokenService tokenService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {

        @NotNull
        private Long uid;

        @NotBlank
        @Length(min = 11, max = 11, message = "手机号格式异常")
        private String phone;
    }

    @PostMapping("/wx/phone")
    public Result<Boolean> change(@Valid @RequestBody Param param) {
        String newPhone = param.phone;
        return redissonUtil.lockAlwaysExec(() -> {
                    User user = service.searchByPhone(newPhone);  //该手机未绑定账号时，user=null

                    //该手机号未被绑定时，执行修改（PS: 先修改库，缓存的旧值，用于防止穿透）
                    if (notRegistered(user)) {
                        user = db.search(param.uid);
                        db.update(param);
                        return success(phoneCache.reloadAndExpire(newPhone, user));
                    }
                    throw new BusinessException("修改用户手机号失败");
                },
                redisson.getSpinLock(USER.LOCK.key(param.uid)),
                50000,
                50000,
                MILLISECONDS
        );
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginChangePasswordParam {

        @NotBlank
        private String password;

        @NotNull
        @Max(9999)
        @Min(1000)
        private Integer code;
    }
    @PostMapping("/login/pwd")
    public Result<Boolean> loginChangePassword(@Valid @RequestBody LoginChangePasswordParam param) throws InterruptedException, IllegalArgumentException {
        PhoneUtil.checkPhoneCodeFormat(String.valueOf(param.code));
        String phone = service.loginUser().getPhone();
        Integer code = codeCache.getCode(phone);
        codeCache.checkIsCanSendCode(code);
        Preconditions.checkArgument(code.equals(param.code), "验证码不可用，请重新发送");
        codeCache.delCode(phone);
        return redissonUtil.lockExec(
                () -> {
                    User user = service.searchByPhone(phone);
                    if(nonNull(user)) {
                        String password = service.encryptPassword(param.getPassword(), user.getSalt());
                        if(service.updatePasswordByID(password, user.getId())) {
                            userCache.load(user, 7, DAYS);
                            return success();
                        }
                    }
                    return failed();
                },
                () -> failed(500, null, "无法获取登录锁，详情请联系客服"),
                redisson.getSpinLock(USER_LOGIN_PHONE.LOCK.key(phone)),
                50000,
                50000,
                MILLISECONDS
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordParam {

        @NotBlank
        private String password;

        @NotNull
        @Max(9999)
        @Min(1000)
        private Integer code;
    }

    @PostMapping("/pwd")
    public Result<Boolean> changePassword(@Valid @RequestBody ChangePasswordParam param) throws InterruptedException, IllegalArgumentException {
        String phone = service.loginUser().getPhone();
        Integer code = codeCache.getCode(phone);
        codeCache.checkIsCanSendCode(code);
        Preconditions.checkArgument(code.equals(param.code), "验证码不可用，请重新发送");
        codeCache.delCode(phone);
        return redissonUtil.lockExec(
                () -> {
                    User user = service.searchByPhone(phone);
                    if(nonNull(user)) {
                        String password = service.encryptPassword(param.getPassword(), user.getSalt());
                        if(service.updatePasswordByID(password, user.getId())) {
                            userCache.load(user, 7, DAYS);
                            tokenService.clearLoginFlag(user.getId());
                            return success();
                        }
                    }
                    return failed();
                },
                () -> failed(500, null, "无法获取登录锁，详情请联系客服"),
                redisson.getSpinLock(USER_LOGIN_PHONE.LOCK.key(phone)),
                50000,
                50000,
                MILLISECONDS
        );
    }
}
