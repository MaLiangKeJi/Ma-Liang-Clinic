package com.bbs.auth.service;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.db.DbRuntimeException;
import com.bbs.auth.controller.ResourceController;
import com.bbs.auth.entity.Resource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;

import java.util.List;


/**
* @author 路晨霖
* @description 针对表【resource(资源)】的数据库操作Service
* @createDate 2023-12-28 18:33:47
*/
public interface ResourceService extends IService<Resource> {

    Result<Page<Resource>> search(ResourceController.SearchListParam param);

    void saveUserConfig(Long uid, String name, String value, String errorMsg) throws DbRuntimeException;

    Resource searchUserConfig(Long uid, String name);

    Result<ResourceController.UserConfig> searchUserConfig();

    Result<List<Tree<Long>>> searchPageTree();

    Result<List<Resource>> searchPageList(String name);

    Result<Resource> searchPage(Long id);

    Result<Boolean> delete(Long id);

    List<Tree<Long>> convertToTree(List<Resource> resources);

    List<Resource> getByUserId(Long userId);
}
