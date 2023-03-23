package com.training.twittertokafkaservice.client;

import com.training.twittertokafkaservice.exception.KafkaClientException;
import com.training.twittertokafkaservice.properties.CustomKafkaProperties;
import com.training.twittertokafkaservice.properties.RetryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdminClients {

    private final CustomKafkaProperties customKafkaProperties;

    private final KafkaProperties kafkaProperties;

    private final AdminClient adminClient;

    private final RetryTemplate retryTemplate;

    private final WebClient webClient;

    public void createTopics() {
        try {
            var topicsCreationResult = retryTemplate.execute(this::doCreateTopics);
            // TODO creae if not exists
            log.debug("Topics creation result: {}", topicsCreationResult.all().get());
        } catch (Exception e) {
            throw new KafkaClientException("Failed to create kafka topics", e);
        }
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        return adminClient.createTopics(customKafkaProperties.topics()
                .stream()
                .map(this::createTopic)
                .toList());
    }

    private NewTopic createTopic(CustomKafkaProperties.Topic topic) {
        return new NewTopic(topic.getName(),
                topic.getPartitions(),
                topic.getReplicas());
    }

    @Retryable
    public void checkSchemaRegistryAvailable() {
        webClient.get()
                .uri(kafkaProperties.buildProducerProperties().get("schema.registry.url").toString())
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                    throw new KafkaClientException("Schema registry is not available");
                });
    }

    @Retryable
    public void checkTopicsCreated() {
        try {
            var missingTopics = new HashSet<String>();
            var existingTopics = adminClient.listTopics().listings().get()
                    .stream()
                    .map(TopicListing::name)
                    .collect(Collectors.toSet());

            for (var topic : customKafkaProperties.topics()) {
                if (!existingTopics.contains(topic.getName())) {
                    missingTopics.add(topic.getName());
                }
            }

            if (!missingTopics.isEmpty()) {
                throw new KafkaClientException("Some topics are missing: " + missingTopics);
            }
        } catch (Exception e) {
            throw new KafkaClientException("Failed to retrieve kafka topics", e);
        }
    }

}
