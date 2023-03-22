package com.training.twittertokafkaservice.client;

import com.training.twittertokafkaservice.exception.KafkaClientException;
import com.training.twittertokafkaservice.properties.CustomKafkaProperties;
import com.training.twittertokafkaservice.properties.RetryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdminClients {

    private final CustomKafkaProperties customKafkaProperties;

    private final AdminClient adminClient;

    private final RetryTemplate retryTemplate;

    private final RetryProperties retryProperties;

    private final WebClient webClient;

    public void createTopics() {
        try {
            retryTemplate.execute(this::doCreateTopics);
        } catch (Exception e) {
            throw new KafkaClientException("Failed to create kafka topics");
        }
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        return adminClient.createTopics(List.of(createTopic(customKafkaProperties.getTwitterTopic())));
    }

    private NewTopic createTopic(CustomKafkaProperties.Topic topic) {
        return new NewTopic(topic.getName(),
                topic.getPartitions(),
                topic.getReplicas());
    }

    // TODO add @Retryable
    public void checkSchemaRegistryAvailable() {
        webClient.get()
                .uri(customKafkaProperties.getSchemaRegistryUrl())
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                    throw new KafkaClientException("Schema registry is not available");
                });
    }

    public void checkTopicsCreated() {

    }

}
