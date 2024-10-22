package com.bbs.auth.conf;


import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry)
    {


    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE","GET","POST")
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                .allowCredentials(false).maxAge(3600);
    }

    @Bean
    public LoginInterceptor createLoginInterceptor() {
        return new LoginInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/test"+ "/**")
                .excludePathPatterns("/admin/content"+ "/**")
                .excludePathPatterns("/content/query"+ "/**")
                .excludePathPatterns("/salary/temp/export"+ "/**")
                .excludePathPatterns("/weixin/receive"+ "/**")
                .excludePathPatterns("/weixin/getQRCode"+ "/**")
                .excludePathPatterns("/weixin/check"+ "/**")
                .excludePathPatterns("/phone/**")
                .excludePathPatterns("/user" + "/**")//注册
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/system/router/user")
                .excludePathPatterns("/me")
                .excludePathPatterns("/province")
        ;
    }
}

