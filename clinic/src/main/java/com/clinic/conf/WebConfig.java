package com.clinic.conf;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${prescription.file.path}")
    private String filePath;

    @Value("${prescription.file.down.prefix}")
    private String fileDownPrefix;

    @Value("${prescription.image.path}")
    private String imagePath;

    @Value("${prescription.image.down.prefix}")
    private String imageDownPrefix;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {

        // 本地文件上传路径，映射
        registry
                .addResourceHandler(fileDownPrefix + "/**", imageDownPrefix + "/**")
                .addResourceLocations("file:" + filePath + File.separator, "file:" + imagePath + File.separator)
        ;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(fileDownPrefix  + "/**")
//        registry.addMapping("/**")
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
                .excludePathPatterns("/import/excel"+ "/**")
                .excludePathPatterns("/admin/content"+ "/**")
                .excludePathPatterns("/content/query"+ "/**")
                .excludePathPatterns("/salary/temp/export"+ "/**")
                .excludePathPatterns("/weixin/pay/notification"+ "/**")
                .excludePathPatterns(fileDownPrefix + "/**")
                .excludePathPatterns(imageDownPrefix + "/**")
                .excludePathPatterns("/ai/**")
                .excludePathPatterns("/open/**")
                .excludePathPatterns("/user")
        ;
    }
}

