package com.bbs.auth.app.user.check;

import com.bbs.Result;
import com.bbs.auth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 校验用户是否注册
 */
@RestController
@RequestMapping
public class CheckRegister {

    @Resource
    private UserService userService;

    @GetMapping("/check/register")
    public Result<Boolean> isRegister(@RequestParam Long phone) {
        return Result.success(userService.checkRegister(phone));
    }
}
