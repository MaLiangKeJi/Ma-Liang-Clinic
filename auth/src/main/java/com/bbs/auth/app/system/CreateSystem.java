package com.bbs.auth.app.system;

import com.bbs.Result;
import com.bbs.auth.converter.SystemConverter;
import com.bbs.auth.entity.System;
import com.bbs.auth.service.SystemService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
@RequestMapping
public class CreateSystem {

    @Resource
    private SystemService service;

    @Resource
    private SystemConverter converter;

    @Resource
    private UserService userService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Param implements Serializable {

        @NotBlank
        private String name;

        private String description;

        @NotNull
        private Long admin;

        private Long pid;
    }

    @PutMapping("/system")
    public Result<Boolean> create(@Valid @RequestBody Param param) {
        System system = converter.toEntity(param);
        system.setCreateBy(userService.loginUser().getId());
        return Result.success(service.save(system));
    }
}
