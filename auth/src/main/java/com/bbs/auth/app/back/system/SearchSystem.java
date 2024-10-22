package com.bbs.auth.app.back.system;

import com.bbs.Result;
import com.bbs.auth.entity.System;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.SystemService;
import com.bbs.auth.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController(value = "searchBackSystem")
@RequestMapping
public class SearchSystem {

    @Resource
    private SystemService systemService;
    
    @Resource
    private UserService userService;

    @Cacheable(cacheNames = "back::system")
    @GetMapping("/back/system")
    public Result<List<System>> search() {
        List<System> systems = systemService.lambdaQuery().list();
        if(systems.size() > INTEGER_ZERO) fillAdmin(systems);
        return Result.success(systems);
    }

        private void fillAdmin(List<System> systems) {
        Set<Long> userIds = new HashSet<>();
        systems.forEach(system -> {
            if(nonNull(system.getAdminId())) userIds.add(system.getAdminId());
        });
        if(userIds.size() > INTEGER_ZERO) {
            Map<Long, User> userIdMap = userService.searchMap(userIds);
            if(userIdMap.size() > INTEGER_ZERO) {
                systems.forEach(system -> system.setAdmin(userIdMap.get(system.getAdminId())));
            }
        }
    }
}
