package com.bbs.auth.cache.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import com.bbs.auth.cache.SystemResourceCache;
import com.bbs.auth.util.RedisUtil;
import com.bbs.auth.entity.Resource;
import com.bbs.auth.enums.ResourceTypeEnum;
import com.bbs.auth.service.ResourceService;
import com.bbs.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.bbs.auth.cache.impl.SystemResourceCacheImpl.ResourceCache.isTopLevelResource;
import static com.bbs.auth.cache.impl.SystemResourceCacheImpl.ResourceCache.notTopLevelResource;
import static com.bbs.auth.enums.RedisKeys.*;
import static com.bbs.auth.util.RedisUtil.Redisson.lockExec;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.math.NumberUtils.*;

@Slf4j
public class SystemResourceCacheImpl implements SystemResourceCache {

    @Slf4j
    @Component
    public static class ResourceCache implements SystemResourceCache.ResourceCache {

        @javax.annotation.Resource
        private ResourceService service;

        @javax.annotation.Resource
        private RedisUtil redis;

        private final String key = RESOURCE.key();

        @Override
        public List<Resource> reloadNotLock() {
            List<Resource> resources = service.lambdaQuery().eq(Resource::getType, ResourceTypeEnum.PAGE.getType()).list();
            redis.hashSet(key, resources.stream().collect(Collectors.toMap(
                    item -> item.getId().toString(),
                    JSONUtil::toJsonPrettyStr
            )));
            return resources;
        }

        @Override
        public Resource search(Long id) {
            return Opt.of(redis.hashGet(key, id.toString())).map(obj -> (Resource)obj).orElseGet(() -> service.getById(id));
        }

        @Override
        public List<Resource> search(List<Object> ids) {
            List<Resource> result = new ArrayList<>(ids.size());

            List<Object> objects = redis.hashGet(key, ids);
            List<Long> emptyIds = new ArrayList<>();
            List<Integer> emptyIdIdxList = new ArrayList<>();
            for (int index = 0; index < objects.size(); index++) {
                Object obj = objects.get(index);
                if(nonNull(obj)) {
                    result.add(JSONUtil.toBean((String)obj, Resource.class));
                } else {
                    // 保存 Redis 中不存在资源的 id 与 index，并结果集填充为 null
                    emptyIds.add((Long) ids.get(index));
                    emptyIdIdxList.add(index);
                    result.add(null);
                }
            }
            // 批量查询不存在资源，回填结果集
            if(emptyIds.size() > 0) {
                List<Resource> emptyResource = service.listByIds(emptyIds);
                set(emptyResource);
                for (int index = 0; index < emptyResource.size(); index++) {
                    Integer emptyIdIdx = emptyIdIdxList.get(index);
                    result.set(emptyIdIdx, emptyResource.get(index));
                }
            }
            return result;
        }

        @Override
        public void set(Resource resource) {
            redis.hashSet(key, resource.getId(), JSONUtil.toJsonPrettyStr(resource));
        }

        @Override
        public void set(List<Resource> resources) {
            redis.hashSet(key, resources.stream().collect(Collectors.toMap(Resource::getIdStr, JSONUtil::toJsonPrettyStr)));
        }

        @Override
        public Boolean removeAll() {
            log.info("系统资源: 正在清除...");
            return redis.delete(key);
        }

        public static Boolean isTopLevelResource(Resource resource) {
            return resource.getParentId().equals(LONG_ZERO);
        }

        public static Boolean notTopLevelResource(Resource resource) {
            return !isTopLevelResource(resource);
        }
    }

    @Slf4j
    @Component
    public static class ResourceTreeCacheImpl implements ResourceTreeCache {

        @javax.annotation.Resource
        private ResourceService service;

        @javax.annotation.Resource
        private RedisUtil redis;

        @javax.annotation.Resource
        private ResourceCache resourceCache;

        @javax.annotation.Resource
        private RedissonClient redisson;

        private final String key = RESOURCE_PAGE_TREE.key();

        private final String lockKey = RESOURCE.LOCK.key();

        @Override
        public Result<Boolean> reload() {
            try {
                return RedisUtil.Redisson.lockExec(redisson.getSpinLock(lockKey), 500, 2000, MILLISECONDS, () -> {
                    if(resourceCache.removeAll() && removeAll()) {
                        List<Resource> resources = resourceCache.reloadNotLock();//重新加载全部资源到缓存

                        //依靠缓存中的资源，重新加载资源树
                        redis.zSet(key, resources.stream().map(resource -> ZSetOperations.TypedTuple.of(
                                resource.getId().toString(),
                                resource.getParentId().doubleValue()
                        )).collect(Collectors.toSet()));
                        return Result.success();
                    }
                    log.error("系统资源树: 重载失败，无法清除缓存！！！");
                    return Result.failed("系统资源树: 重载失败，无法清除缓存！！！");
                });
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return Result.failed();
            }
        }

        @Override
        public Resource searchParent(Resource resource) {
            return resourceCache.search(resource.getParentId());
        }

        @Override
        public List<Resource> searchChildren(Resource resource) {
            Long score = resource.getId();
            Set<String> ids = redis.zGet(key, score, score);
            return nonNull(ids) && ids.size() > 0 ? resourceCache.search(new ArrayList<>(ids)) : new ArrayList<>();
        }

        @Override
        public void recursionSearchFillParent(Resource resource, List<Resource> container, Map<Long, Resource> idMap) {
            if(idMap.containsKey(resource.getParentId())) return;
            Resource parent = searchParent(resource);
            container.add(parent);
            if(notTopLevelResource(parent)) {
                recursionSearchFillParent(parent, container, idMap);
            }
        }

        @Override
        public void recursionSearchFillChildren(Resource resource, List<Resource> container) {
            List<Resource> children = searchChildren(resource);
            if(children.size() > 0) {
                container.addAll(children);
                children.forEach(child -> {
                    if(nonNull(child)) recursionSearchFillChildren(child, container);
                });
            }
        }

        @Override
        public List<Tree<Long>> searchByUID(Long uid) {
            List<Resource> resources = service.getByUserId(uid);    //直接关联的资源
            resources.addAll(fillLackResources(resources));     //填充缺少的资源（父级 & 子级资源）
            return service.convertToTree(resources);    // to tree
        }

        /**
         * 填充缺少的资源（父级 & 子级资源）
         * @param resources 直接关联的资源
         * @return 缺少的、间接关联的资源
         */
        private List<Resource> fillLackResources(List<Resource> resources) {
            Map<Long, Resource> idMap = toIdMap(resources);     // Map<UID, Resource>
            List<Resource> lackResources = new ArrayList<>();   //缺少的资源（间接与用户账户关联，比如子级资源）
            resources.forEach(resource -> {
                if(isTopLevelResource(resource)) {
                    String str = redis.get(RESOURCE_PAGE_TREE_ALL_CHILDREN.key(resource.getId()));
                    lackResources.addAll(
                            isLoadCache(str) ?
                                    JSONUtil.toBean(str, new TypeReference<List<Resource>>() {}, true) :
                                    searchFillChildren(resource)
                    );
                } else {
                    recursionSearchFillParent(resource, lackResources, idMap);
                }
            });
            return lackResources;
        }

        private List<Resource> searchFillChildren(Resource resource) {
            List<Resource> temporaryContainer = new ArrayList<>();      //临时容器
            recursionSearchFillChildren(resource, temporaryContainer);   //递归查询【子级资源】，并填充到【临时容器】
            loadToCache(RESOURCE_PAGE_TREE_ALL_CHILDREN.key(resource.getId()), temporaryContainer);    //加载到缓存
            return temporaryContainer;
        }


        private void loadToCache(String key, List<Resource> container) {
            redis.set(key, JSONUtil.toJsonPrettyStr(container));
        }

        private boolean isLoadCache(String str) {
            return nonNull(str);
        }

        private Map<Long, Resource> toIdMap(List<Resource> resources) {
            return resources.stream().collect(Collectors.toMap(Resource::getId, resource -> resource));
        }

        @Override
        public Boolean removeAll() {
            log.info("系统资源树: 正在清除...");
            return redis.delete(key);
        }
    }
}
