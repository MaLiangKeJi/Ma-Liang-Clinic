package com.bbs.auth.service;


import com.bbs.auth.controller.RoleController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.auth.entity.Role;
import com.bbs.Result;

/**
 *
 */
public interface RoleService extends IService<Role> {

    Result<Page<Role>> search(RoleController.QueryRoleParam param);
}
