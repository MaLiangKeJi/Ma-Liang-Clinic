package com.bbs.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.db.DbRuntimeException;
import com.bbs.auth.cache.impl.SystemResourceCacheImpl;
import com.bbs.auth.controller.ResourceController;
import com.bbs.auth.entity.Resource;
import com.bbs.auth.entity.RoleGroup;
import com.bbs.auth.entity.RoleResource;
import com.bbs.auth.entity.UserGroup;
import com.bbs.auth.enums.ResourceNames;
import com.bbs.auth.enums.ResourceTypeEnum;
import com.bbs.auth.mapper.ResourceMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.service.*;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;

/**
* @author 路晨霖
* @description 针对表【resource(资源)】的数据库操作Service实现
* @createDate 2023-12-28 18:33:47
*/
@Service
public class ResourceServiceImpl extends MPJBaseServiceImpl<ResourceMapper, Resource>
    implements ResourceService {

    @javax.annotation.Resource
    private UserService userService;

    @Override
    public Result<Page<Resource>> search(ResourceController.SearchListParam param) {
        return Result.success(lambdaQuery()
                .eq(nonNull(param.getType()), Resource::getType, param.getType())
                .page(param.toPage()));
    }

    @Override
    public void saveUserConfig(Long uid, String name, String value, String errorMsg) throws DbRuntimeException {
        if(!save(new Resource(
                ResourceTypeEnum.USER_CONFIG.getType(),
                name,
                uid.toString(),
                value)
        )) throw new DbRuntimeException(errorMsg);
    }

    @Override
    public Resource searchUserConfig(Long uid, String name) {
        return lambdaQuery()
                .eq(Resource::getType, ResourceTypeEnum.USER_CONFIG.getType())
                .eq(Resource::getName, name)
                .eq(Resource::getCode, uid.toString()).one();
    }

    @Override
    public Result<ResourceController.UserConfig> searchUserConfig() {
        List<Resource> resources = lambdaQuery()
                .eq(Resource::getType, ResourceTypeEnum.USER_CONFIG.getType())
                .eq(Resource::getCode, userService.loginUser().getId())
                .list();
        if(nonNull(resources) && resources.size() > 0) {
            ResourceController.UserConfig config = new ResourceController.UserConfig();
            resources.forEach(resource -> {
                if(ResourceNames.UserConfig.CLINIC_NAME.getName().equals(resource.getName())) {
                    config.setClinicName(resource);
                }
                if(ResourceNames.UserConfig.STOCK_COUNT_TYPE.getName().equals(resource.getName())) {
                    config.setStockStateCountRule(resource);
                }
                if(ResourceNames.UserConfig.STOCK_COUNT_VAL.getName().equals(resource.getName())) {
                    config.setStockStateCountVal(resource);
                }
                if(ResourceNames.UserConfig.STOCK_COUNT_UNIT.getName().equals(resource.getName())) {
                    config.setStockStateCountUnit(resource);
                }
                if(ResourceNames.UserConfig.DRUG_EXPIRE_VAL.getName().equals(resource.getName())) {
                    config.setExpiryAlert(resource);
                }
            });
            return Result.success(config);
        }
        return Result.successNull();
    }

    @Override
    public Result<List<Tree<Long>>> searchPageTree() {
        List<Resource> resources = lambdaQuery().eq(Resource::getType, ResourceTypeEnum.PAGE.getType()).list();
        return Result.success(convertToTree(resources));
    }

    @Override
    public List<Tree<Long>> convertToTree(List<Resource> resources) {
        if(CollectionUtil.isNotEmpty(resources)){
            return TreeUtil.build(resources.stream().map(resource -> {
                TreeNode<Long> treeNode = new TreeNode<>(resource.getId(), resource.getParentId(), resource.getName(), resource.getWeight());
                treeNode.setExtra(new HashMap<String, Object>() {{
                    put("icon", resource.getDescription());
                    put("title", resource.getName());
                    put("path", resource.getValue());
                    put("label", resource.getName());
                    put("code", resource.getCode());
                    put("description", resource.getDescription());
                    put("value", resource.getValue());
                    put("isHide", resource.getState());
                }});
                return treeNode;
            }).collect(Collectors.toList()), LONG_ZERO);
        }
        return null;
    }

    @Override
    public Result<List<Resource>> searchPageList(String name) {
        return Result.success(lambdaQuery()
                .eq(Resource::getType, ResourceTypeEnum.PAGE.getType())
                .eq(StringUtils.isNotBlank(name), Resource::getName, name)
                .list()
        );
    }

    @Override
    public Result<Resource> searchPage(Long id) {
        Resource resource = getById(id);

        Long parentId = resource.getParentId();
        if(nonNull(parentId) && parentId != 0L) {
            resource.setParent(getById(parentId));
        }
        return Result.success(resource);
    }

    @Override
    public Result<Boolean> delete(Long id) {
        Long childrenNum = lambdaQuery()
                .eq(Resource::getParentId, id)
                .count();
        if(childrenNum > 0) return Result.failed("资源存在子级，无法删除");
        return Result.of(removeById(id));
    }

    @javax.annotation.Resource
    private UserGroupService userGroupService;

    @javax.annotation.Resource
    private RoleGroupService roleGroupService;

    @javax.annotation.Resource
    private RoleResourceService roleResourceService;

    @Lazy
    @javax.annotation.Resource
    private SystemResourceCacheImpl.ResourceCache resourceCache;

    @Override
    public List<Resource> getByUserId(Long userId) {

        List<UserGroup> userGroups = userGroupService.lambdaQuery().eq(UserGroup::getUserId, userId).list();
        List<Long> groupIds = userGroups.stream().map(UserGroup::getGroupId).collect(Collectors.toList());

        List<RoleGroup> roleGroups = roleGroupService.lambdaQuery().in(RoleGroup::getGroupId, groupIds).list();
        List<Long> roleIds = roleGroups.stream().map(RoleGroup::getRoleId).collect(Collectors.toList());

        List<RoleResource> roleResources = roleResourceService.lambdaQuery().in(RoleResource::getRoleId, roleIds).list();

        List<Object> resourceIds = roleResources.stream().map(roleResource -> (Object) roleResource.getResourceId()).collect(Collectors.toList());

        return resourceCache.search(resourceIds);
    }
}




