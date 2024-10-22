package com.bbs.auth.mapper;

import com.bbs.auth.dto.RoleGroupVo;
import com.bbs.auth.entity.RoleGroup;

import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

/**
 * @Entity com.clinic.entity.RoleGroup
 */
public interface RoleGroupMapper extends MPJBaseMapper<RoleGroup> {
    List<RoleGroupVo> selectRoleGroupNameList();
}




