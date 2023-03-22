package com.training.twittertokafkaservice.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

import static com.training.twittertokafkaservice.util.Constants.APP_NAME;

@Data
@Configuration
@ConfigurationProperties(prefix = APP_NAME + ".kafka")
public class CustomKafkaProperties {
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private List<Topic> topics;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private Topic twitterTopic;

    @Data
    public static class Topic {
        private String name;
        private int partitions;
        private short replicas;
    }

    public List<Topic> topics() {
        if (Objects.isNull(topics)) {
            topics = List.of(twitterTopic);
        }
        return topics;
    }

}
