package com.bbs.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@Slf4j
@MapperScan({"com.bbs.auth.mapper"})
@EnableAsync
@EnableRetry    //启用操作重试
@EnableCaching
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient  //Nacos 服务发现
public class Auth implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Auth.class);
        application.addInitializers(context -> {
            Environment env = context.getEnvironment();
            String appName = env.getProperty("spring.application.name");
            String port = env.getProperty("server.port");
            String filePath = System.getProperty("user.home") + File.separator + ".dubbo" + File.separator + appName + port;
            System.setProperty("dubbo.meta.cache.filePath", filePath);
            System.setProperty("dubbo.mapping.cache.filePath",filePath);
        });
        application.run(args);
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("。。。。。。。。。。。。。容器初始化完毕。。。。。。。。。。。。。。");
    }

}