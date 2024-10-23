package pl.kurs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Value("${books.import.core-pool-size}")
    private int corePoolSize;

    // todo dodac te wartosci do properties, albo ustawic je w klasie konfiguracyjnej
    @Bean
    public Executor booksImportExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("books-import-");
        executor.initialize();
        return executor;
    }
}