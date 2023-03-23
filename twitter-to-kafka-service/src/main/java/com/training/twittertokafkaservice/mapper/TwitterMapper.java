package com.training.twittertokafkaservice.mapper;

import com.training.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterMapper {

    public TwitterAvroModel mapToEvent(Status status) {
        return TwitterAvroModel.newBuilder()
                .setId(status.getId())
                .setText(status.getText())
                .setUserId(status.getUser().getId())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }

}
