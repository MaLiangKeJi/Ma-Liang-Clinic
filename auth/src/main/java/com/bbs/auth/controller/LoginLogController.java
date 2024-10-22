package com.bbs.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.LoginLog;
import com.bbs.auth.service.LoginLogService;
import com.bbs.auth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static java.util.Objects.nonNull;

@RequestMapping
@RestController
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    @GetMapping("/back/login/log")
    public Result<Page<LoginLog>> list(@RequestParam(required = false) Long userId, Integer current, Integer size) {
        userService.checkLoginUserIsAdmin();
        Page<LoginLog> page = loginLogService.lambdaQuery()
                .eq(nonNull(userId), LoginLog::getUserId, userId)
                .page(new Page<>(current, size));
        return Result.success(page);
    }



}
