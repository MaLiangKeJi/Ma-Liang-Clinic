package com.clinic.conf;


import com.bbs.api.auth.User;
import com.bbs.api.auth.UserAPI;
import com.bbs.exception.ReLoginException;
import com.clinic.util.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @DubboReference
    private UserAPI userAPI;

    @Value("${auth.api.verify.token}")
    private String tokenName;


    @Override
    public boolean preHandle(@NotNull @org.jetbrains.annotations.NotNull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws ReLoginException, IOException {
        String token = request.getHeader(tokenName);

        //用户可查处方药页面放过
        String uri = request.getRequestURI().toString();
        if (uri.startsWith("/drug/wx"))
            return true;
        if (uri.startsWith("/drug/open"))
            return openDrugProcess(request.getQueryString());

        //支付接口处理
        String referer = request.getHeader("referer");
        if (uri.equals("/pay") && StringUtils.isNotBlank(referer) && referer.contains("/open/drug?"))
            return payProcess(request.getParameter("uid"));

        if(StringUtils.isNotBlank(token)) {
            log.debug("拦截器request.getRequestURI(){}",request.getRequestURI());
            if(request.getRequestURI().contains("/weixin/pay/notification")){
                return true;
            }
            User user;
            try {
                user = userAPI.getUserByToken(request.getHeader(tokenName));
            } catch (Exception e) {
                throw new ReLoginException();
            }
            if (nonNull(user)){
                LoginUser.set(user);
                return true;
            }
        }
        response.sendError(401,"请重新登陆");
        return false;
    }

    /**
     * 给药房的处方药拦截处理
     */
    private boolean openDrugProcess(String queryStr) {
        String[] queryParams = queryStr.split("&");
        Long uid = 0L;
        for (String query : queryParams) {
            if (query.startsWith("uid")) {
                uid = Long.valueOf(query.split("=")[NumberUtils.INTEGER_ONE]);
                break;
            }
        }
        if (uid < NumberUtils.LONG_ONE)
            return false;

        List<User> userTmpList = userAPI.getUserList(Collections.singletonList(uid));
        if (ObjectUtils.isEmpty(userTmpList) || userTmpList.size() > NumberUtils.INTEGER_ONE)
            return false;

        User user = userTmpList.get(NumberUtils.INTEGER_ZERO);
        if (nonNull(user)) {
            LoginUser.set(user);
            return true;
        }
        return false;
    }

    private boolean payProcess(String uid) {
        //getPay(Long)接口直接过
        if (StringUtils.isBlank(uid))
            return true;

        List<User> userTmpList = userAPI.getUserList(Collections.singletonList(Long.valueOf(uid)));
        if (ObjectUtils.isEmpty(userTmpList) || userTmpList.size() > NumberUtils.INTEGER_ONE)
            return false;

        User user = userTmpList.get(NumberUtils.INTEGER_ZERO);
        if (nonNull(user)) {
            LoginUser.set(user);
            return true;
        }
        return false;
    }

    /**
     * 接口访问结束后，从ThreadLocal中删除用户信息
     */
    @Override
    public void afterCompletion(@org.jetbrains.annotations.NotNull HttpServletRequest request, @org.jetbrains.annotations.NotNull HttpServletResponse response, @org.jetbrains.annotations.NotNull Object handler, Exception ex) {
        LoginUser.remove();
    }

}
