package com.bbs.auth.service;

import cn.hutool.core.lang.tree.Tree;
import com.bbs.auth.entity.SystemRouter;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

/**
* @author 路晨霖
* @description 针对表【system_router(路由：元数据)】的数据库操作Service
* @createDate 2024-07-02 19:02:39
*/
public interface SystemRouterService extends MPJBaseService<SystemRouter> {

    List<SystemRouter> searchBySystemId(Long systemId);

    List<Tree<Long>> toTree(List<SystemRouter> routers);

    List<Tree<Long>> searchTreeBySystemId(Long systemId);
}
