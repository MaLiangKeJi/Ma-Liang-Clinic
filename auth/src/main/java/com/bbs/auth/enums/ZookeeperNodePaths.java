package com.bbs.auth.enums;

/**
 * Zookeeper 节点路径
 */
public class ZookeeperNodePaths {

    private ZookeeperNodePaths() {}

    /**
     * 缓存配置
     */
    public static class CacheConf {

        public static class Token {
            /**
             * 登录凭证缓存过期时间：随机上限
             */
            public static final String TIMEOUT_MAX = "/conf/cache/token/timeout/max";

            /**
             * 登录凭证缓存过期时间：随机下限
             */
            public static final String TIMEOUT_MIN = "/conf/cache/token/timeout/min";
        }

        public static class User {
            /**
             * 用户信息缓存过期时间：随机上限
             */
            public static final String TIMEOUT_MAX = "/conf/cache/user/timeout/max";

            /**
             * 用户信息缓存过期时间：随机下限
             */
            public static final String TIMEOUT_MIN = "/conf/cache/user/timeout/min";

            /**
             * UID 与其他用户【类唯一】字段映射：过期时间随机上限
             */
            public static final String UID_MAP_TIMEOUT_MAX = "/conf/cache/user/map/timeout/max";

            /**
             * UID 与其他用户【类唯一】字段映射：过期时间随机下限
             */
            public static final String UID_MAP_TIMEOUT_MIN = "/conf/cache/user/map/timeout/min";
        }
    }

    /**
     * 锁配置
     */
    public static class LockConf {
        public static class UserCache {
            public static final String WAIT = "/conf/lock/cache/user/wait";
            public static final String LEASE = "/conf/lock/cache/user/lease";
        }
    }

    /**
     * 微信小程序
     */
    public static class VXProgram {
        /**
         * 应用 ID
         */
        public static final String APP_ID = "/vx/appid";
        /**
         * 密钥
         */
        public static final String SECRET = "/vx/secret";
    }

    /**
     * 短信配置
     */
    public static class Captcha {

        /**
         * 验证码过期时间（单位：分钟）
         */
        public static final String CODE_TIMEOUT = "/conf/captcha/timeout";

        /**
         * 验证码发送间隔（单位：秒）
         */
        public static final String CODE_SEND_INTERVAL = "/conf/captcha/send/interval";

        public static class Alibaba {

            public static final String SIGN_NAME = "/conf/captcha/alibaba/signName";

            public static final String TEMPLATE_CODE = "/conf/captcha/alibaba/templateCode";

            public static final String ACCESS_KEY = "/conf/captcha/alibaba/accessKey";

            public static final String ACCESS_KEY_SECRET = "/conf/captcha/alibaba/accessKeySecret";

            public static final String ENDPOINT = "/conf/captcha/alibaba/endpoint";
        }
    }
}
