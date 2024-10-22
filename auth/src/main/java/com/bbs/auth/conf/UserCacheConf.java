package com.bbs.auth.conf;

import com.bbs.auth.entity.UserBind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bbs.auth.enums.RedisKeys.*;

/**
 * 用户缓存配置（Map<UID, UserJSON>）
 */
@Slf4j
@Component
public class UserCacheConf {

    public String key(com.bbs.auth.entity.User user) {
        return key(user.getId());
    }

    public String key(Long uid) {
        return USER.key(uid);
    }

    /**
     * 用户信息缓存：最小过期时间
     */
    public Integer minTimeout() {
        return 5;
    }
    /**
     * 用户信息缓存：最大过期时间
     */
    public Integer maxTimeout() {
        return 7;
    }

    /**
     * 与用户 UID 关联的其他 Map 缓存（Map<关联ID, UID>）：最小过期时间
     */
    public Integer associationMapMinTimeout() {
        return 5;
    }

    /**
     * 与用户 UID 关联的其他 Map 缓存（Map<关联ID, UID>）：最大过期时间
     */
    public Integer associationMapMaxTimeout() {
        return 7;
    }




    //------------------------------------------------------------------------------------------------------------------



    /**
     * 微信 OpenID 与用户 UID 关联的 Map 缓存（Map<WXOpenID, Info>）
     */
    @Slf4j
    @Component
    public static class WXOpenIDMap {

        public String key(UserBind userBind) {
            return key(userBind.getOpenId());
        }

        public String key(String openID) {
            return USER_OPEN_ID_AND_ID_MAP.key(openID);
        }
    }




    //------------------------------------------------------------------------------------------------------------------




    /**
     * 手机号与用户 UID 关联的 Map 缓存（Map<Login, Info>）
     */
    @Slf4j
    @Component
    public static class PhoneMap {
        public String key(com.bbs.auth.entity.User user) {
            return key(user.getPhone());
        }
        public String key(String phone) {
            return key(Long.valueOf(phone));
        }

        public String key(Long phone) {
            return USER_PHONE_AND_ID_MAP.key(phone);
        }
    }
}
