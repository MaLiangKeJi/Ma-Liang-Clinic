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

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;

@RestController("moveSystemRouter")
@RequestMapping
public class Move {

    @Resource
    private SystemRouterService systemRouterService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        private Long routerId;

        private Long targetRouterId;
    }

    @PostMapping("/back/system/router/move")
    public Result<Boolean> move(@RequestBody Param param) {
        Long parentId = nonNull(param.getRouterId()) ? param.getTargetRouterId() : LONG_ZERO;
        Long count = systemRouterService.lambdaQuery()
                .eq(SystemRouter::getParentId, parentId)
                .count();

        return Result.success(systemRouterService.lambdaUpdate()
                .set(SystemRouter::getParentId, parentId)
                .set(SystemRouter::getWeight, count +1)
                .eq(SystemRouter::getId, param.getRouterId())
                .update());
    }
}
