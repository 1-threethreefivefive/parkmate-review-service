package com.parkmate.reviewservice.kafka.constant;

public class KafkaTopics {

    public static final String REVIEW_CREATED = "review.review.created";

    public static final String REVIEW_REACTION_UPDATED = "review.review-reactions.updated";

    public static final String REVIEW_UPDATED = "review.review.updated";

    public static final String REVIEW_DELETED = "review.review.deleted";

    private KafkaTopics() {

    }
}
