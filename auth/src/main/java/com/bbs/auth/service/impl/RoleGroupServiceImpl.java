package com.bbs.auth.service.impl;

import com.bbs.auth.dto.RoleGroupVo;
import com.bbs.auth.entity.RoleGroup;
import com.bbs.auth.mapper.RoleGroupMapper;
import com.bbs.auth.service.RoleGroupService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class RoleGroupServiceImpl extends MPJBaseServiceImpl<RoleGroupMapper, RoleGroup>
    implements RoleGroupService{

    @Override
    public List<RoleGroupVo> selectRoleAndGroupNameList() {
        return baseMapper.selectRoleGroupNameList();
    }
}




