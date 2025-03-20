package io.github.orcunbalcilar.springcurrency;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Value("${currency.task.executor.core-pool-size}")
    private int corePoolSize;

    @Value("${currency.task.executor.queue-capacity}")
    private int queueCapacity;

    @Bean(name = "currencyTaskExecutor")
    public Executor taskExecutor() {
        logger.info("Creating Async Task Executor with corePoolSize={}", corePoolSize);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize * 2);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("ConversionService-");

        // Reject policy: Caller runs
        executor.setRejectedExecutionHandler((r, e) -> {
            logger.warn("Task rejected, thread pool is full. Running in caller thread.");
            if (r != null) {
                r.run();
            }
        });

        executor.initialize();
        return executor;
    }
}