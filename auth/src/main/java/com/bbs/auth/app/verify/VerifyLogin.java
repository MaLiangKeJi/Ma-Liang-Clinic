package com.bbs.auth.app.verify;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.bbs.Result;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.bbs.Result.*;
import static com.bbs.enums.CodeEnum.SUCCESS_USER_LOGIN;

@Slf4j
@RestController
@RequestMapping
public class VerifyLogin {

    private final TokenService tokenService;

    @Resource
    @Lazy
    private UserCache cache;

    private final UserConverter converter;

    private final UserService service;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTokenVerifyParam {
        @NotNull
        private String token;
    }

    /**
     * 验证token
     * 用户访问其他服务时需要携带登录成功时返回的令牌(token)来完成服务间调用
     * @param param UserTokenVerifyParam
     * @return 用户信息（校验失败返回 null）
     */
    @PostMapping("/verify")
    public Result<UserVO> verify(@RequestBody UserTokenVerifyParam param) throws InterruptedException {
        log.debug("[UserAPI::verifyToken] 请求 param={}", param);
        UserVO userVO = tokenService.parseToken(param.getToken().split(" ")[1]);
        Long uid = userVO.getId();
        String loginFlagStr = tokenService.getLoginFlag(uid);
        if(StringUtils.isNotBlank(loginFlagStr)) {
            if(Method.differLessThanOneDay(loginFlagStr)) {
                User user = cache.search(uid);
                if(service.userIsUsable(user)) {
                    userVO = converter.toVO(user);
                    log.debug("[UserAPI::verifyToken] 响应 user={}", userVO);
                    return success(SUCCESS_USER_LOGIN, userVO);
                }
            }
        }
        return failed(400, "请重新登录");
    }

    private static class Method {

        private static Boolean differLessThanOneDay(String loginFlagStr) {
            return DateUtil.between(
                    DateUtil.beginOfDay(DateUtil.parse(loginFlagStr)),
                    DateUtil.beginOfDay(new Date()),
                    DateUnit.DAY,
                    false
            ) == 0;
        }
    }

    @Autowired
    public VerifyLogin(TokenService tokenService, UserConverter converter, UserService service) {
        this.tokenService = tokenService;
        this.converter = converter;
        this.service = service;
    }
}
