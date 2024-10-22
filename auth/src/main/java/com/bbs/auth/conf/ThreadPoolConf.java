package com.bbs.auth.conf;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConf {

    @Bean(name = "async_method")
    public ThreadPoolExecutor asyncMethodThreadPool() {
        return new ThreadPoolExecutor(
                NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE,
                5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new DefaultThreadFactory("async_method"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
