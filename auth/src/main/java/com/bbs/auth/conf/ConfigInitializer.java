package com.bbs.auth.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigInitializer implements CommandLineRunner {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosServerAddr;

    @Value("${spring.redis.host}")
    private String redisAddress;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Nacos Server Address: " + nacosServerAddr);
        System.out.println("Redisson Redis Address: " + redisAddress);
    }
}
