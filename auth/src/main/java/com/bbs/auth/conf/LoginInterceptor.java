package com.bbs.auth.conf;


import com.bbs.auth.service.UserService;
import com.bbs.auth.util.LoginUser;
import com.bbs.exception.ReLoginException;
import com.bbs.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(@NotNull @org.jetbrains.annotations.NotNull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws ReLoginException, IOException {
        log.debug("拦截器request.getRequestURI(){}",request.getRequestURI());
        if(request.getRequestURI().contains("/weixin/clinic/pay/notification")){
            return true;
        }
        UserVO userVO = LoginUser.get();
        if (isNull(userVO)){
            userVO = userService.loginUser();
            if (nonNull(userVO)){
                LoginUser.set(userVO);
                return true;
            }
        }else{
            return true;
        }
        return false;
    }


    /**
     * 接口访问结束后，从ThreadLocal中删除用户信息
     */
    @Override
    public void afterCompletion(@org.jetbrains.annotations.NotNull HttpServletRequest request,
                                @org.jetbrains.annotations.NotNull HttpServletResponse response,
                                @org.jetbrains.annotations.NotNull Object handler,
                                Exception ex) {
        LoginUser.remove();
    }

}
