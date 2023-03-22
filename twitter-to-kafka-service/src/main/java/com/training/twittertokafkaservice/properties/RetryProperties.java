package com.training.twittertokafkaservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.training.twittertokafkaservice.util.Constants.APP_NAME;

@Data
@Configuration
@ConfigurationProperties(prefix = APP_NAME + ".retry")
public class RetryProperties {
    private long initialIntervalMs;
    private long maxIntervalMs;
    private  double multiplier;
    private int maxAttempts;
}
