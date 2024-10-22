package com.bbs.auth.cache;

import java.util.List;

public interface PermissionCache {


    interface UserGroup {

        void reload();

        List<com.bbs.auth.entity.UserGroup> search(Long uid);
    }


    interface RoleGroup {

        void reload();

        List<com.bbs.auth.entity.RoleGroup> search(List<Long> groupIds);
    }

    interface RoleResource {

        void reload();

        List<com.bbs.auth.entity.RoleResource> search(List<Long> roleIds);
    }
}
