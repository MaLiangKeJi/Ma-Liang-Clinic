package com.bbs.auth.app.user.update;

import com.bbs.Result;
import com.bbs.auth.cache.user.PhoneCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.UserService;
import com.bbs.exception.ReLoginException;
import com.bbs.vo.UserVO;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RestController
@RequestMapping
public class UpdateUser {

    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {

        @Length(max = 100)
        private String name;

        private String phone;

        @Max(value = 1, message = "性别只能是0或1")
        @Min(value = 0, message = "性别只能是0或1")
        private Integer sex;

        @Pattern(regexp = "^([1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])\\d{3}([0-9]|X)$|^([1-9]\\d{7}(0\\d|10|11|12)([0-2][1-9]|30|31)\\d{3}$))", message = "身份证号码格式不正确")
        private String idCard;

        @Max(value = 9999, message = "验证码格式错误")
        @Min(value = 1001, message = "验证码格式错误")
        private Integer code;
    }

    @Resource
    private UserConverter converter;
    @Resource
    private UserService userService;
    @Resource
    private UserCache userCache;
    @Resource
    private PhoneCache phoneCache;

    @PostMapping("/user")
    public Result<UserVO> update(@RequestBody @Valid Param param) throws IllegalArgumentException, ReLoginException {
        User user = converter.toEntity(param);
        Long loginUserId = userService.loginUser().getId();
        if(needResetPhone(param)) {
            checkPhoneAndCode(param);
        }
        fillUID(loginUserId, user);
        update(user);
        removeCache(loginUserId, param);
        return Result.success(research(loginUserId));
    }

    private boolean needResetPhone(Param param) {
        return isNotBlank(param.phone);
    }

    private void checkPhoneAndCode(Param param) throws IllegalArgumentException {
        Preconditions.checkArgument(java.util.regex.Pattern.matches(PHONE_REGEX, param.phone), "请输入正确的手机号格式");
        Preconditions.checkArgument(nonNull(param.code), "修改手机号，必须输入验证码");
        userService.checkPhoneCodeThrow(Long.parseLong(param.phone), param.code);
    }

    private void fillUID(Long loginUserId, User user) {
        user.setId(loginUserId);
    }

    private void update(User user) {
        userService.updateById(user);
    }

    private UserVO research(Long loginUserId) {
        return converter.toVO(userService.search(loginUserId));
    }

    private void removeCache(Long loginUserId, Param param) {
        userCache.remove(loginUserId);
        if(isNotBlank(param.phone)) {
            phoneCache.remove(param.phone);
        }
    }
}
