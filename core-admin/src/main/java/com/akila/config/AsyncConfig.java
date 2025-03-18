package com.akila.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Core pool size
        executor.setMaxPoolSize(20);  // Max pool size
        executor.setQueueCapacity(100); // Queue capacity
        executor.setThreadNamePrefix("TransferThread-");
        executor.initialize();
        return executor;
    }
}
