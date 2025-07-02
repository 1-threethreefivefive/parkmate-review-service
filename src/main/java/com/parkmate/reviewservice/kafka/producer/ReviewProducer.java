package com.parkmate.reviewservice.kafka.producer;

import com.parkmate.reviewservice.kafka.constant.KafkaTopics;
import com.parkmate.reviewservice.kafka.event.CreateReviewEvent;
import com.parkmate.reviewservice.kafka.event.ReactionUpdatedEvent;
import com.parkmate.reviewservice.kafka.event.ReviewDeletedEvent;
import com.parkmate.reviewservice.kafka.event.ReviewUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewProducer {

    private final KafkaTemplate<String, CreateReviewEvent> createReviewKafkaTemplate;
    private final KafkaTemplate<String, ReactionUpdatedEvent> reviewReactionKafkaTemplate;
    private final KafkaTemplate<String, ReviewUpdatedEvent> reviewUpdatedKafkaTemplate;
    private final KafkaTemplate<String, ReviewDeletedEvent> reviewDeletedKafkaTemplate;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCreateReviewEvent(CreateReviewEvent event) {
        createReviewKafkaTemplate.send(
                KafkaTopics.REVIEW_CREATED,
                event.getReviewUuid(),
                event
        );
        log.info("[Kafka] Sent CreateReviewEvent to topic '{}': {}", KafkaTopics.REVIEW_CREATED, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReviewReactionUpdatedEvent(ReactionUpdatedEvent event) {
        reviewReactionKafkaTemplate.send(
                KafkaTopics.REVIEW_REACTION_UPDATED,
                event.getReviewUuid(),
                event
        );
        log.info("[Kafka] Sent ReactionUpdatedEvent to topic '{}': {}", KafkaTopics.REVIEW_REACTION_UPDATED, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReviewUpdatedEvent(ReviewUpdatedEvent event) {
        reviewUpdatedKafkaTemplate.send(
                KafkaTopics.REVIEW_UPDATED,
                event.getReviewUuid(),
                event
        );
        log.info("[Kafka] Sent ReviewUpdatedEvent to topic '{}': {}", KafkaTopics.REVIEW_UPDATED, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReviewDeletedEvent(ReviewDeletedEvent event) {
        reviewDeletedKafkaTemplate.send(
                KafkaTopics.REVIEW_DELETED,
                event.getReviewUuid(),
                event
        );
        log.info("[Kafka] Sent ReviewDeletedEvent to topic '{}': {}", KafkaTopics.REVIEW_DELETED, event);
    }
}