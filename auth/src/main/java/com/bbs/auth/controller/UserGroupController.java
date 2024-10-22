package com.bbs.auth.controller;

import com.bbs.Result;
import com.bbs.auth.entity.UserGroup;
import com.bbs.auth.service.UserGroupService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/group")
public class UserGroupController {


    private UserGroupService userGroupService;


    /**
     * 查询所有用户组
     */
    @GetMapping
    public Result queryUserGroup(){
        return Result.success(userGroupService.list());
    }


    /**
     * 添加/修改用户组
     */
    @PostMapping
    public Result addUserGroup(@RequestBody UserGroup userGroup){
        return Result.success(userGroupService.saveOrUpdate(userGroup));
    }


    /**
     * 删除用户组
     */
    @DeleteMapping
    public Result deleteUserGroup(@RequestBody Long id){
        return Result.success(userGroupService.removeById(id));
    }







    @Resource
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }
}
