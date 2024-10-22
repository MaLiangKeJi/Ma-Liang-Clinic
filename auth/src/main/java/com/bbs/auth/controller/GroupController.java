package com.bbs.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.Group;
import com.bbs.auth.service.GroupService;
import com.bbs.vo.BaseParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/group")
public class GroupController {


    private GroupService groupService;


    /**
     * 查询所有组
     */
    @GetMapping
    public Result<Page<Group>> queryGroup(BaseParam param){
        return Result.success(groupService.page(param.toPage()));
    }


    /**
     * 添加/修改组
     */
    @PostMapping
    public Result<Boolean> addGroup(@RequestBody Group group){
        return Result.success(groupService.saveOrUpdate(group));
    }


    /**
     * 删除组
     */
    @DeleteMapping
    public Result<Boolean> deleteGroup(@RequestBody  Long id){
        return Result.success(groupService.updateById(new Group().setId(id).setState(1)));
    }

    @Resource
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}
