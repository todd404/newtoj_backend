package com.todd.toj_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Bean(name = "asyncExecutor")
    public TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 设置核心线程池大小
        executor.setMaxPoolSize(10); // 设置最大线程池大小
        executor.setQueueCapacity(100); // 设置队列容量
        executor.setThreadNamePrefix("AsyncThread-"); // 设置线程名前缀
        executor.initialize();
        return executor;
    }
}