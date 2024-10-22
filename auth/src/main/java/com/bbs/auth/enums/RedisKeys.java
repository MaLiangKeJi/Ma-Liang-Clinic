package com.bbs.auth.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

/**
 * Redis 缓存 keys
 * <a href="https://developer.aliyun.com/article/531067">规范参考：阿里云</a>
 */
@Getter
@AllArgsConstructor
public enum RedisKeys {

    USER("user", "用户缓存"),
    USER_EMAIL_AND_ID_MAP("user:email", "用户ID与邮箱映射（邮箱-ID）"),
    USER_OPEN_ID_AND_ID_MAP("user:vx", "用户ID与微信openId映射（openId-ID）"),

    USER_PHONE_AND_ID_MAP("user:phone", "用户ID与手机号映射（phone-ID）"),

    USER_UID_AND_TOKEN_MAP("user:token", "用户ID与登录Token映射（token-uid）"),

    USER_PHONE_REGISTER("user:register", "用户手机号注册"),

    USER_PHONE_CODE("user:phone:code", "用户手机号短信"),
    USER_LOGIN_COMPANY("login:company:", "用户登录绑定公司"),

    USER_LOGIN_PHONE("user:login:phone", "用户手机号登录"),

    USER_COMPANY("user_company", "用户的公司信息"),

    COMPANY_STRUCTURE("company_structure", "公司结构信息"),

    RESOURCE("resource", "资源"),
    RESOURCE_PAGE_TREE("resource:page:tree", "页面资源 Tree"),
    RESOURCE_PARENT_ID_MAP("resource:parent:id:map", "页面资源映射（Map<parentId,resourceId>）"),
    RESOURCE_PAGE_TREE_ALL_CHILDREN("resource:page:tree:all_children", "页面资源 Tree: 具体节点的子级资源"),


    PERMISSION_USER_GROUP("permission:user:group", "权限: 用户 & 组关联"),

    PERMISSION_USER_GROUP_UID_MAP("permission:user:group:uid:map", "权限: 用户 & 组关联映射用户 ID"),
    PERMISSION_ROLE_GROUP("permission:role:group", "权限: 角色 & 组关联"),

    PERMISSION_ROLE_GROUP_ID_MAP("permission:role:group:id:map", "权限: 角色 & 组关联映射组 ID"),
    PERMISSION_ROLE_RESOURCE("permission:role:resource", "权限: 角色 & 资源关联")
    ;

    private final String prefix;

    private final String description;

    public final Lock LOCK;

    RedisKeys(String prefix, String description) {
        this.prefix = prefix;
        this.description = description;
        this.LOCK = new Lock(prefix, description);
    }

    private static final String LOCK_SUFFIX = "lock";

    public String key() {
        return prefix;
    }

    /**
     * key
     * @param mark 业务标识 / 表名 / 主键
     * @return key
     */
    public String key(String mark) {
        return prefix + ":" + mark;
    }
    public String key(Long mark) {
        return key(mark.toString());
    }
    public String key(Integer mark) {
        return key(mark.toString());
    }

    @Override
    public String toString() {
        return key();
    }

    @Data
    @AllArgsConstructor
    public static class Lock {

        private final String prefix;

        private final String description;

        /**
         * 锁 key
         * @param mark 业务标识 / 表名 / 主键
         * @return key
         */
        public String key(String mark) {
            return prefix + mark + ":" + LOCK_SUFFIX;
        }
        public String key(Long mark) {
            return key(mark.toString());
        }

        public String key(Integer mark) {
            return key(mark.toString());
        }

        public String key() {
            return prefix + ":" + LOCK_SUFFIX;
        }

        @Override
        public String toString() {
            return key();
        }
    }

    public static final Map<String, RedisKeys> map = EnumUtil.getEnumMap(RedisKeys.class);
}
