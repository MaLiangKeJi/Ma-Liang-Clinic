package com.bbs.auth.app.system;

import com.bbs.Result;
import com.bbs.auth.converter.SystemConverter;
import com.bbs.auth.entity.System;
import com.bbs.auth.service.SystemService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class UpdateAdmin {

    @Resource
    private SystemService service;

    @Resource
    private SystemConverter converter;

    @Resource
    private UserService userService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Param {

        private Long id;

        private Long admin;
    }

    @PostMapping("/system")
    public Result<Boolean> update(Param param) {
        System system = converter.toEntity(param);
        system.setUpdateBy(userService.loginUser().getId());
        return Result.success(service.save(system));
    }
}
