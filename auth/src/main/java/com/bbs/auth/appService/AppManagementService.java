package com.bbs.auth.appService;

import com.bbs.auth.service.GroupService;
import com.bbs.auth.service.RoleGroupService;
import com.bbs.auth.service.RoleService;
import com.bbs.auth.service.UserGroupService;
import com.bbs.auth.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AppManagementService {

    private GroupService groupService;

    private RoleService roleService;

    private UserService userService;

    private UserGroupService userGroupService;

    private RoleGroupService roleGroupService;















    @Resource
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    @Resource
    public void setRuleService(RoleService roleService) {
        this.roleService = roleService;
    }
    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Resource
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }
    @Resource
    public void setRoleGroupService(RoleGroupService roleGroupService) {
        this.roleGroupService = roleGroupService;
    }
}
