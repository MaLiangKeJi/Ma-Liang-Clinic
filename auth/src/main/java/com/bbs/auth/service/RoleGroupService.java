package com.bbs.auth.service;

import com.bbs.auth.dto.RoleGroupVo;
import com.bbs.auth.entity.RoleGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface RoleGroupService extends IService<RoleGroup> {
    List<RoleGroupVo> selectRoleAndGroupNameList();
}
