package com.bbs.auth.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbs.auth.entity.SystemRouter;
import com.bbs.auth.service.SystemRouterService;
import com.bbs.auth.mapper.SystemRouterMapper;
import com.bbs.auth.service.UserService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.*;

/**
* @author 路晨霖
* @description 针对表【system_router(路由：元数据)】的数据库操作Service实现
* @createDate 2024-07-02 19:02:39
*/
@Service
public class SystemRouterServiceImpl extends MPJBaseServiceImpl<SystemRouterMapper, SystemRouter>
    implements SystemRouterService{

    @Resource
    private UserService userService;

    @Override
    public List<SystemRouter> searchBySystemId(Long systemId) {
        boolean loginUserIsNotAdmin = (!userService.loginUserIsAdmin());
        return list(new LambdaQueryWrapper<SystemRouter>()
                .eq(nonNull(systemId), SystemRouter::getSystemId, systemId)
                .eq(loginUserIsNotAdmin, SystemRouter::getIsAdmin, INTEGER_ZERO)
        );
    }

    @Override
    public List<Tree<Long>> toTree(List<SystemRouter> routers) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setDeep(5);
        treeNodeConfig.setParentIdKey("parentId");
        treeNodeConfig.setChildrenKey("children");

        return TreeUtil.build(routers, LONG_ZERO, treeNodeConfig, (router, tree) -> {
            if(nonNull(router)){
                tree.setId(router.getId());
                tree.setParentId(router.getParentId());
                tree.setWeight(router.getWeight());
                tree.setName(router.getCode());
                tree.putExtra("code", router.getCode());

                if(isNotBlank(router.getIconName())) tree.putExtra("iconName", router.getIconName());
                if(isNotBlank(router.getTitle())) tree.putExtra("title", router.getTitle());
                if(nonNull(router.getType())) tree.putExtra("type", router.getType());
                if(nonNull(router.getSystemId())) tree.putExtra("systemId", router.getSystemId());
                if(isNotBlank(router.getPath())) tree.putExtra("path", router.getPath());
                if(isNotBlank(router.getComponentPath())) tree.putExtra("componentPath", router.getComponentPath());
            }
        });
    }

    @Override
    public List<Tree<Long>> searchTreeBySystemId(Long systemId) {
        return toTree(searchBySystemId(systemId));
    }
}




