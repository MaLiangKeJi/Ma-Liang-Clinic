package com.bbs.auth.app.back.system.router;

import com.bbs.Result;
import com.bbs.auth.entity.SystemRouter;
import com.bbs.auth.service.SystemRouterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("updateSystemRouter")
@RequestMapping
public class Update {

    @Resource
    private SystemRouterService systemRouterService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        private List<Long> ids;

        private Integer type;
    }

    @PostMapping("/back/system/router/batch")
    public Result<Boolean> batchUpdate(@RequestBody Param param) {
        return Result.success(systemRouterService.lambdaUpdate()
                .set(SystemRouter::getType, param.getType())
                .in(SystemRouter::getId, param.getIds())
                .update());
    }

    @PostMapping("/back/system/router")
    public Result<Boolean> update(@RequestBody SystemRouter router) {
        return Result.success(systemRouterService.updateById(router));
    }
}
