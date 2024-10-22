package com.bbs.auth.app.back.system.router;

import com.bbs.Result;
import com.bbs.auth.entity.SystemRouter;
import com.bbs.auth.service.SystemRouterService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("addSystemRouter")
@RequestMapping
public class Add {

    @Resource
    private SystemRouterService systemRouterService;

    @PutMapping("/back/system/router")
    public Result<Boolean> search(@RequestBody SystemRouter systemRouter) {
        Long count = systemRouterService.lambdaQuery()
                .eq(SystemRouter::getParentId, systemRouter.getParentId())
                .count();
        systemRouter.setWeight(count +1);
        return Result.success(systemRouterService.save(systemRouter));
    }
}
