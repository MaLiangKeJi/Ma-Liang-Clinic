package com.bbs.auth.conf;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class DruidConfig {
    @Bean
    public ServletRegistrationBean regisDruid() {
        //固定写法，配置访问路径
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //配置登录信息，固定写法
        HashMap<String, String> initParams = new HashMap<>();
        //账号和密码的key是固定的
        initParams.put("loginUsername", "root");
        initParams.put("loginPassword", "shenbimaliang");

        //允许谁可以访问
        initParams.put("allow", "");
        bean.setInitParameters(initParams);
        return bean;
    }
}