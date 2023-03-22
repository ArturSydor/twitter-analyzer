package com.training.twittertokafkaservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.training.twittertokafkaservice.util.Constants.APP_NAME;

@Data
@Configuration
@ConfigurationProperties(prefix = APP_NAME + ".kafka")
public class CustomKafkaProperties {

    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private Topic twitterTopic;

    @Data
    public static class Topic {
        private String name;
        private int partitions;
        private short replicas;
    }

}
