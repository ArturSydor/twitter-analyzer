package com.training.twittertokafkaservice;

import com.training.twittertokafkaservice.properties.ServiceConfigurationProperties;
import com.training.twittertokafkaservice.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final ServiceConfigurationProperties serviceConfigurationProperties;

    private final StreamRunner streamRunner;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info(serviceConfigurationProperties.getTweetsKeywords().toString());
        streamRunner.start();
    }
}
