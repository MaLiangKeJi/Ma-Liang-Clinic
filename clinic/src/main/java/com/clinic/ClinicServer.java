package com.clinic;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@Slf4j
@MapperScan({"com.clinic.mapper"})
@EnableAsync
@EnableSpringUtil
@EnableCaching
@ComponentScan(value = { "com.bbs", "com.clinic"})
@SpringBootApplication
@EnableDubbo
@EnableScheduling
@EnableDiscoveryClient  //Nacos 服务发现
public class ClinicServer implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ClinicServer.class);
        application.addInitializers(context -> {
            Environment env = context.getEnvironment();
            String appName = env.getProperty("spring.application.name");
            String filePath = System.getProperty("user.home") + File.separator + ".dubbo" +File.separator + appName;
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
