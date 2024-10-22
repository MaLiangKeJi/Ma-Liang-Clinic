package com.bbs.auth.app.user.router;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.bbs.Result;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.entity.System;
import com.bbs.auth.entity.SystemCompany;
import com.bbs.auth.entity.SystemRouter;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.SystemCompanyService;
import com.bbs.auth.service.SystemRouterService;
import com.bbs.auth.service.SystemService;
import com.bbs.auth.service.UserService;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController("searchMySystemRouter")
@RequestMapping
@Slf4j
public class SearchSystemRouter {

    @Resource
    private SystemRouterService systemRouterService;
    @Resource
    private UserService userService;
    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;
    @Resource
    private SystemCompanyService systemCompanyService;
    @Resource
    private SystemService systemService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class VO {

        private List<SystemRouter> list;

        private List<Tree<Long>> tree;
    }

    @GetMapping("/system/router/user")
    public Result<VO> search(@RequestParam String systemCode,Long time) {
        log.debug("time:{}", time);
        User user = userService.loginEntityUser();
        System system = systemService.searchBySystemCode(systemCode);
        Long systemId = system.getId();
        List<SystemRouter> systemRouters = searchRouter(systemId);
        if(!user.isSupperAdmin()&&CollUtil.isNotEmpty(systemRouters)){
            systemRouters = systemRouters.stream().filter(o->
                    !o.getId().equals(77L)
                    &&!o.getId().equals(84L)
                    &&!o.getId().equals(100L)).collect(Collectors.toList());
            return Result.success(new VO(systemRouters, systemRouterService.toTree(systemRouters)));
        }
        return Result.success(new VO(systemRouters, systemRouterService.toTree(systemRouters)));
    }

    private List<SystemRouter> searchRouter(Long systemId) {
        return systemRouterService.searchBySystemId(systemId);
    }

    private Long searchCompanyId(Long uid) throws IllegalArgumentException {
        Long companyId = bindLoginCompanyCache.get(uid);
        userIsBindCompany(companyId);
        return companyId;
    }

    private void userIsBindCompany(Long companyId) throws IllegalArgumentException {
        Preconditions.checkArgument(nonNull(companyId), "用户未绑定公司信息，请先绑定公司信息");
    }

    private void tryCreateSystemCompany(Long systemId, Long companyId) {
        SystemCompany systemCompany = systemCompanyService.search(systemId, companyId);
        if(isNull(systemCompany)) {
            systemCompany = new SystemCompany(companyId, systemId, INTEGER_ZERO);
            systemCompanyService.save(systemCompany);
        }
    }
}
