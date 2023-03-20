package com.training.twittertokafkaservice.runner;

import com.training.twittertokafkaservice.listener.TwitterToKafkaStatusListener;
import com.training.twittertokafkaservice.model.StatusMock;
import com.training.twittertokafkaservice.properties.ServiceConfigurationProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class MockTwitterStreamRunner implements StreamRunner {


    private final ServiceConfigurationProperties configurationProperties;
    private final TwitterToKafkaStatusListener twitterToKafkaStatusListener;

    private final EasyRandom easyRandom;

    public MockTwitterStreamRunner(ServiceConfigurationProperties configurationProperties,
                                   TwitterToKafkaStatusListener twitterToKafkaStatusListener) {
        this.configurationProperties = configurationProperties;
        this.twitterToKafkaStatusListener = twitterToKafkaStatusListener;
        this.easyRandom = new EasyRandom(getParameters());
    }

    @Override
    public void start() {
        log.info("Started streaming tweets for such keywords: {}", configurationProperties.getTweetsKeywords());
        simulateTwitterStream();
    }

    private void simulateTwitterStream() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                twitterToKafkaStatusListener.onStatus(createRandomTweet());
                sleep();
            }
        });
    }

    private void sleep() {
        try {
            Thread.sleep(configurationProperties.getMock().getDelay());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private Status createRandomTweet() {
        var mockTweet = easyRandom.nextObject(StatusMock.class);
        try {
            return TwitterObjectFactory.createStatus(mockTweet.asJson());
        } catch (TwitterException e) {
            log.error("Failed to create mock tweet.", e);
            throw new IllegalStateException(e);
        }
    }

    private EasyRandomParameters getParameters() {
        var parameters = new EasyRandomParameters();
        parameters.stringLengthRange(configurationProperties.getMock().getMinTextLength(),
                configurationProperties.getMock().getMaxTextLength());
        parameters.randomize(Long.class, () -> ThreadLocalRandom.current().nextLong(1, 1000));
        return parameters;
    }
}
