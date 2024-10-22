package com.bbs.auth.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.ResourceType;
import com.bbs.auth.service.ResourceService;
import com.bbs.auth.service.ResourceTypeService;
import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Resource
    private ResourceService service;
    @Resource
    private ResourceTypeService typeService;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SearchListParam extends BaseParam {

        private Long type;
    }

    @PostMapping
    public Result<Boolean> addResource(@RequestBody com.bbs.auth.entity.Resource resource) {
        return Result.of(service.saveOrUpdate(resource));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchAddResourceParam {

        private List<com.bbs.auth.entity.Resource> resources;
    }

    @PutMapping("/batch")
    public Result<Boolean> batchAddResource(@RequestBody BatchAddResourceParam param) {
        return Result.of(service.saveOrUpdateBatch(param.resources));
    }

    @GetMapping("/list")
    public Result<Page<com.bbs.auth.entity.Resource>> searchList(SearchListParam param) {
        return service.search(param);
    }

    @GetMapping("/type/list")
    public Result<Page<ResourceType>> searchTypeList(BaseParam param) {
        return typeService.search(param);
    }


    @GetMapping("/type/list/join")
    public Result<Page<ResourceType>> searchTypeListJoin(BaseParam param) {
        return typeService.searchJoin(param);
    }

    @Data
    public static class UserConfig {
        private com.bbs.auth.entity.Resource clinicName;

        private com.bbs.auth.entity.Resource stockStateCountRule;

        private com.bbs.auth.entity.Resource stockStateCountVal;

        private com.bbs.auth.entity.Resource stockStateCountUnit;

        private com.bbs.auth.entity.Resource expiryAlert;
    }

    @GetMapping("/config")
    public Result<UserConfig> searchUserConfig() {
        return service.searchUserConfig();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageVO {

        private Long id;

        private String icon;

        private String title;

        private String path;

        private List<PageVO> children;
    }

    @GetMapping("/page")
    public Result<com.bbs.auth.entity.Resource> searchPage(@Valid @NotNull Long id) {
        return service.searchPage(id);
    }

    @GetMapping("/page/list")
    public Result<List<com.bbs.auth.entity.Resource>> searchPage(String name) {
        return service.searchPageList(name);
    }

    @GetMapping("/page/tree")
    public Result<List<Tree<Long>>> searchPageTree() {
        return service.searchPageTree();
    }

    @DeleteMapping ("/page")
    public Result<Boolean> delPage(@NotNull Long id) {
        return service.delete(id);
    }
}
