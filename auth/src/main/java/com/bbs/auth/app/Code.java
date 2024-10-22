package com.bbs.auth.app;

import com.bbs.auth.util.captcha.CaptchaUtil;
import com.bbs.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 短信验证码
 */
@Slf4j
@RestController
@RequestMapping
public class Code {

    @Resource
    private CaptchaUtil util;

    @GetMapping("/phone")
    public Result<Boolean> send(@RequestParam("phone") String phone) {
        util.send(phone);
        return Result.success();
    }
}
