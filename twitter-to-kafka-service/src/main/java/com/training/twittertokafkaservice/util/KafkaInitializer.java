package com.training.twittertokafkaservice.util;

import com.training.twittertokafkaservice.client.KafkaAdminClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaInitializer {

    private final KafkaAdminClients kafkaAdminClients;

    public void init() {
        kafkaAdminClients.createTopics();
        // FIXME kafkaAdminClients.checkTopicsCreated();
        kafkaAdminClients.checkSchemaRegistryAvailable();
    }

}
