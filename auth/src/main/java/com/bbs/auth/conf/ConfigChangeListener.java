package com.bbs.auth.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 检测能否读取到 Nacos 配置
 */
@Slf4j
@RefreshScope
@Component
public class ConfigChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        if (keys.contains("spring.redis.host")) {
            // 这里可以处理配置变化逻辑，包括读取失败的逻辑处理
            log.info("Redis 配置：addr={}:{}", redisHost, redisPort);
        } else {
            log.error("Redis 配置：未读取到！");
        }
    }
}
