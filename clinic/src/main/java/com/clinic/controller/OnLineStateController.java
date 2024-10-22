package com.clinic.controller;

import com.bbs.Result;
import com.clinic.cache.on.line.OnLineStateCache;
import com.clinic.util.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OnLineStateController {

    @Resource
    private OnLineStateCache cache;

    /**
     * 设置在线状态
     * @param onLine
     * @return
     */
    @PutMapping("/on/line")
    public Result<Boolean> setOnLine(Boolean onLine) {
        cache.setOnLine(LoginUser.getId(),onLine);
        return Result.success(true);
    }

    @GetMapping("/on/line")
    public Result<Boolean> isOnLine() {
        return Result.success(cache.isOnLine(LoginUser.getId()));
    }

}
