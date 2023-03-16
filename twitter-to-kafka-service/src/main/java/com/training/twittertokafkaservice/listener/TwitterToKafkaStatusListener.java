package com.training.twittertokafkaservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
public class TwitterToKafkaStatusListener extends StatusAdapter {
    @Override
    public void onStatus(Status status) {
      log.info("Processing tweet: {}", status);
    }
}
