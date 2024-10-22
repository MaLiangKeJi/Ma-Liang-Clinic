package com.bbs.auth.controller;

import com.bbs.auth.dto.RoleGroupVo;
import com.bbs.Result;
import com.bbs.auth.appService.AppRoleGroupService;
import com.bbs.auth.entity.RoleGroup;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/role/group")
public class RoleGroupController {


    private AppRoleGroupService appRoleGroupService;

    /**
     * 查询所有角色组
     */
    @GetMapping
    public Result<List<RoleGroupVo>> queryRoleGroup(){
        return Result.success(appRoleGroupService.selectRoleAndGroupNameList());
    }

    /**
     * 添加/修改角色组
     */
    @PostMapping
    public Result<Boolean> addRoleGroup(@RequestBody RoleGroup roleGroup){
        return Result.success(appRoleGroupService.saveOrUpdate(roleGroup));
    }

    private static class BatchAddRoleGroupParam {

        List<RoleGroup> roleGroups;
    }

    /**
     * 添加/修改角色组
     */
    @PostMapping("/batch")
    public Result<Boolean> batchAddRoleGroup(@Valid @RequestBody BatchAddRoleGroupParam param){
        return Result.success(appRoleGroupService.saveOrUpdateBatch(param.roleGroups));
    }


    /**
     * 删除角色组
     */
    @DeleteMapping
    public Result<Boolean> deleteRoleGroup(@RequestBody Long id){
        return Result.success(appRoleGroupService.removeById(id));
    }


    @Resource
    public void setAppRoleGroupService(AppRoleGroupService appRoleGroupService) {
        this.appRoleGroupService = appRoleGroupService;
    }
}
