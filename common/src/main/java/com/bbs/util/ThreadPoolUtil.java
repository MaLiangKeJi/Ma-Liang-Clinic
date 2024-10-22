package com.bbs.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolUtil {

    @Bean("asyncMethodThreadPool")
    public ThreadPoolExecutor getThreadPool(){
        return new ThreadPoolExecutor(1, 5, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

}
