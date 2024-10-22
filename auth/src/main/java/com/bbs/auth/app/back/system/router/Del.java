package com.bbs.auth.app.back.system.router;

import com.bbs.Result;
import com.bbs.auth.service.SystemRouterService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("delSystemRouter")
@RequestMapping
public class Del {

    @Resource
    private SystemRouterService systemRouterService;

    @DeleteMapping("/back/system/router/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.of(systemRouterService.removeById(id));
    }
}
