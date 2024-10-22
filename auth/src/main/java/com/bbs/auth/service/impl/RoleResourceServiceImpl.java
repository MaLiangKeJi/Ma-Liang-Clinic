package com.bbs.auth.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.bbs.auth.controller.RoleController;
import com.bbs.auth.entity.Resource;
import com.bbs.auth.mapper.ResourceMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.auth.entity.RoleResource;
import com.bbs.auth.service.RoleResourceService;
import com.bbs.auth.mapper.RoleResourceMapper;
import com.bbs.Result;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 路晨霖
* @description 针对表【role_resource(角色 & 资源关联表)】的数据库操作Service实现
* @createDate 2023-12-28 19:51:38
*/
@Service
public class RoleResourceServiceImpl extends MPJBaseServiceImpl<RoleResourceMapper, RoleResource>
    implements RoleResourceService {

    @javax.annotation.Resource
    private DataSourceTransactionManager transactionManager;

    @javax.annotation.Resource
    private TransactionDefinition transactionDefinition;

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @Override
    public Result<Page<Resource>> search(RoleController.SearchResourcePermissionParam param) {
        return Result.success(resourceMapper.selectJoinPage(param.toPage(), Resource.class, new MPJLambdaWrapper<>(Resource.class)
                .rightJoin(RoleResource.class, RoleResource::getResourceId, Resource::getId, ext -> ext
                        .selectAll(Resource.class)
                )
                .eq(RoleResource::getRoleId, param.getRoleId())
        ));
    }

    @Override
    public Result<Boolean> update(RoleController.AddResourcePermissionParam param) {
        List<Resource> resources = param.getResources();
        Long roleId = param.getRoleId();

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            baseMapper.delete(new MPJLambdaWrapper<>(RoleResource.class).eq(RoleResource::getRoleId, roleId));
            if(!saveBatch(resources.stream().map(resource -> new RoleResource(roleId, resource.getId())).collect(Collectors.toList())))
                throw new DbRuntimeException("更新角色 & 资源失败：保存权限信息失败");
            transactionManager.commit(transaction);
            return Result.success();
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}




