package com.training.twittertokafkaservice.listener;

import com.training.twittertokafkaservice.mapper.TwitterMapper;
import com.training.twittertokafkaservice.producer.TwitterKafkaProducer;
import com.training.twittertokafkaservice.properties.CustomKafkaProperties;
import com.training.twittertokafkaservice.util.KafkaInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterToKafkaStatusListener extends StatusAdapter {

    private final TwitterKafkaProducer twitterKafkaProducer;

    private final TwitterMapper twitterMapper;

    private final CustomKafkaProperties customKafkaProperties;

    @Override
    public void onStatus(Status status) {
        var topic = customKafkaProperties.getTwitterTopic().getName();
        var event = twitterMapper.mapToEvent(status);
        twitterKafkaProducer.send(topic, event);
    }
}
