package com.bbs.auth.app.back.system.router;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbs.Result;
import com.bbs.auth.entity.System;
import com.bbs.auth.entity.SystemRouter;
import com.bbs.auth.service.SystemRouterService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController("searchSystemRouter")
@RequestMapping
public class Search {

    @Resource
    private SystemRouterService systemRouterService;

    @GetMapping("/back/system/router")
    public Result<SystemRouter> searchById(@RequestParam(required = false) Long id) {
        return Result.success(systemRouterService.selectJoinOne(SystemRouter.class, new MPJLambdaWrapper<SystemRouter>()
                .selectAll(SystemRouter.class)
                .leftJoin(System.class, System::getId, SystemRouter::getSystemId, ext -> ext
                        .selectAssociation(System.class, SystemRouter::getSystem)
                )
                .leftJoin(SystemRouter.class, "t2", SystemRouter::getId, SystemRouter::getParentId, ext -> ext
                        .selectAssociation("t2", SystemRouter.class, SystemRouter::getParent)
                )
                .eq(SystemRouter::getId, id)
        ));
    }

    @GetMapping("/back/system/router/list")
    public Result<List<SystemRouter>> searchList(@RequestParam(required = false) Long systemId) {
        return Result.success(systemRouterService.list(new LambdaQueryWrapper<SystemRouter>()
                .eq(nonNull(systemId), SystemRouter::getSystemId, systemId)
        ));
    }

    @GetMapping("/back/system/router/tree")
    public Result<List<Tree<Long>>> searchTree(@RequestParam(required = false) Long systemId) {
        List<SystemRouter> routers = systemRouterService.searchBySystemId(systemId);
        return Result.success(systemRouterService.toTree(routers));
    }
}
