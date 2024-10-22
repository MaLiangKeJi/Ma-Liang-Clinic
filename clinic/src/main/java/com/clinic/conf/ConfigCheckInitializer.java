package com.clinic.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class ConfigCheckInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        checkNacosConf(environment);
        checkRedisConf(environment);
        checkMySQLConf(environment);
    }

    private static void checkNacosConf(ConfigurableEnvironment environment) {
        String nameSpace = environment.getProperty("spring.cloud.nacos.config.namespace");
        String prefix = environment.getProperty("spring.cloud.nacos.config.prefix");
        String fileExtension = environment.getProperty("spring.cloud.nacos.config.file-extension");
        String group = environment.getProperty("spring.cloud.nacos.config.group");
        log.info("环境信息 - Nacos: namespace={}; prefix={}; file-extension={}; group={};", nameSpace, prefix, fileExtension, group);
    }

    private static void checkMySQLConf(ConfigurableEnvironment environment) {
        String dbURL = environment.getProperty("spring.datasource.url");
        String dbUsername = environment.getProperty("spring.datasource.username");
        String dbPassword = environment.getProperty("spring.datasource.password");
        log.info("环境信息 - MySQL: addr={}; username={}; password={};", dbURL, dbUsername, dbPassword);
    }

    private static void checkRedisConf(ConfigurableEnvironment environment) {
        String redisAddress = environment.getProperty("spring.redis.host");
        String redisPort = environment.getProperty("spring.redis.port");
        log.info("环境信息 - Redis: addr={}; port={};", redisAddress, redisPort);
    }
}