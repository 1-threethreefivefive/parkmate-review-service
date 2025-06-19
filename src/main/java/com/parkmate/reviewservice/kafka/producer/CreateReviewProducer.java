package com.parkmate.reviewservice.kafka.producer;

import com.parkmate.reviewservice.kafka.event.CreateReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateReviewProducer {

    private static final String TOPIC_CREATE_REVIEW = "create-review";

    private final KafkaTemplate<String, CreateReviewEvent> kafkaTemplate;

    public void send(CreateReviewEvent event) {
        try {
            kafkaTemplate.send(TOPIC_CREATE_REVIEW, event.getReviewUuid(), event);
            log.info("[Kafka] Successfully sent CreateReviewEvent to topic '{}': {}", TOPIC_CREATE_REVIEW, event);
        } catch (Exception e) {
            log.error("[Kafka] Failed to send CreateReviewEvent: {}", event, e);

        }
    }
}