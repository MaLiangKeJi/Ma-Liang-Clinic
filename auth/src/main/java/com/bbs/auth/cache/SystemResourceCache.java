package com.bbs.auth.cache;

import cn.hutool.core.lang.tree.Tree;
import com.bbs.auth.entity.Resource;
import com.bbs.Result;

import java.util.List;
import java.util.Map;

public interface SystemResourceCache {

    interface ResourceCache {

        /**
         * 重新加载资源到缓存（无锁！！！）
         * @return 全部资源
         */
        List<Resource> reloadNotLock();

        Resource search(Long id);

        List<Resource> search(List<Object> ids);

        void set(Resource resource);

        void set(List<Resource> resources);

        Boolean removeAll();
    }

    interface ResourceTreeCache {

        Result<Boolean> reload();

        void recursionSearchFillParent(Resource resource, List<Resource> container, Map<Long, Resource> idMap);

        void recursionSearchFillChildren(Resource resource, List<Resource> container);

        Resource searchParent(Resource resource);

        List<Resource> searchChildren(Resource resource);

        List<Tree<Long>> searchByUID(Long uid);

        Boolean removeAll();
    }
}
