package com.bbs.auth.service.impl;


import com.bbs.auth.controller.RoleController;
import com.bbs.auth.entity.RoleGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.Role;
import com.bbs.auth.mapper.RoleMapper;
import com.bbs.auth.service.RoleService;
import com.bbs.Result;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 *
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

    @Override
    public Result<Page<Role>> search(RoleController.QueryRoleParam param) {
        if(isNull(param.getGroupId())) {
            return Result.success(page(param.toPage()));
        } else {
            return Result.success(baseMapper.selectJoinPage(param.toPage(), Role.class, new MPJLambdaWrapper<>(Role.class)
                    .rightJoin(RoleGroup.class, RoleGroup::getGroupId, Role::getId, ext  -> ext
                            .selectAll(Role.class)
                    )
                    .eq(RoleGroup::getGroupId, param.getGroupId())
            ));
        }
    }
}




