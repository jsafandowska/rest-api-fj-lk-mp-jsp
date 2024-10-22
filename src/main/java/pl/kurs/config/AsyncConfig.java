package pl.kurs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
public class AsyncConfig {

    @Value("${async.books.import.corePoolSize}")
    private int corePoolSize;

    @Value("${async.books.import.maxPoolSize}")
    private int maxPoolSize;

    @Value("${async.books.import.queueCapacity}")
    private int queueCapacity;

    @Value("${async.books.import.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean
    public Executor booksImportExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}