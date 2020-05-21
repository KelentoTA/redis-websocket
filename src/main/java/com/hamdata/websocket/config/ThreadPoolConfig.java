package com.hamdata.websocket.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean("asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        int coreThreads = 2 * Runtime.getRuntime().availableProcessors() + 1;
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("redis-pool-%d").build();
        return new ThreadPoolExecutor(coreThreads, 300, 2, TimeUnit.MINUTES,
                new SynchronousQueue<>(), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
