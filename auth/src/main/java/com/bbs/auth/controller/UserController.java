package com.bbs.auth.controller;

import cn.hutool.core.lang.tree.Tree;
import com.bbs.auth.cache.SystemResourceCache;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.param.UserParam;
import com.bbs.auth.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping
public class UserController {


    @GetMapping("/user")
    Result<Page<User>> search(UserParam userParam) {
        return service.search(userParam);
    }


    @DeleteMapping("/user/resource")
    public Result<Boolean> reload() {
        return cache.reload();
    }

    @GetMapping("/user/resource/tree")
    public Result<List<Tree<Long>>> searchPageTree() {
        return Result.success(cache.searchByUID(service.loginUser().getId()));
    }

    @Resource
    private UserService service;

    @Resource
    private SystemResourceCache.ResourceTreeCache cache;
}
