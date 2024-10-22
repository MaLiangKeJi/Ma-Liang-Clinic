package com.bbs.auth.conf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCacheConf {


    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "companyStructure",
                "city",
                "back::system",
                "system::router::user",
                "system::code",
                "user::invite",
                "province"
        );
    }
}
