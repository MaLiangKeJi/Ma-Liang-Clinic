package com.clinic.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ThreadPoolConfig {

    @Bean("excelBatchImport")
    public ThreadPoolExecutor excelBatchImport() {
        return new ThreadPoolExecutor(
                0, 4,
                2, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
