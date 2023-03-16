package com.training.twittertokafkaservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class ServiceConfigurationProperties {
    private List<String> tweetsKeywords;

    private MockConfig mock;

    @Data
    public static class MockConfig {
        private int delay;
        private int minTextLength;
        private int maxTextLength;
    }

}
