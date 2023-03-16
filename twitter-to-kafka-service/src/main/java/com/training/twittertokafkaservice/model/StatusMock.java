package com.training.twittertokafkaservice.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class StatusMock {

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private ZonedDateTime createdAt;
    private Long id;
    private String text;
    private Long userId;

    public String asJson() {
        return """
                {
                    "created_at": "%s",
                    "id": "%s",
                    "text": "%s",
                    "user": {
                        "id": "%s"
                    }
                }
                """.formatted(createdAt.format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)), id, text, userId);
    }
}
