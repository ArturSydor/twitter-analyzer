package com.training.twittertokafkaservice.producer;

import com.training.kafka.avro.model.TwitterAvroModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterKafkaProducer {

    private final KafkaTemplate<String, TwitterAvroModel> kafkaTemplate;

    public void send(String topic, TwitterAvroModel model) {
        log.debug("Sending new tweet to kafka topic:{}, value:{}", topic, model);
        kafkaTemplate.send(topic, model)
                .thenApply(sendResult -> {
                    var metaData = sendResult.getRecordMetadata();
                    log.debug("Sent tweet with id={} to topic={}", sendResult.getProducerRecord().value().getId(),
                            metaData.topic());
                    return sendResult;
                })
                .exceptionally(error -> {
                    log.error("Failed to send tweet to kafka", error);
                    return null;
                });
    }

}
