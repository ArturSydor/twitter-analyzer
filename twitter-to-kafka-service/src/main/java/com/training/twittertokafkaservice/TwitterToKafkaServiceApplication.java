package com.training.twittertokafkaservice;

import com.training.twittertokafkaservice.properties.ServiceConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final ServiceConfigurationProperties serviceConfigurationProperties;

    public TwitterToKafkaServiceApplication(ServiceConfigurationProperties serviceConfigurationProperties) {
        this.serviceConfigurationProperties = serviceConfigurationProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(serviceConfigurationProperties.getTweetsKeywords().toString());
    }
}
