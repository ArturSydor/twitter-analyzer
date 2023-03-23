package com.training.twittertokafkaservice.config;

import com.training.twittertokafkaservice.properties.RetryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
@RequiredArgsConstructor
public class RetryConfig {

    private final RetryProperties retryProperties;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .exponentialBackoff(retryProperties.getInitialIntervalMs(),
                        retryProperties.getMultiplier(),
                        retryProperties.getMaxIntervalMs())
                .maxAttempts(retryProperties.getMaxAttempts())
                .build();
    }

}
