package com.parkmate.reviewservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewUpdatedEvent {

    private String reviewUuid;
    private String content;
    private int rating;
    private List<String> imageUrls;
    private LocalDateTime updatedAt;
    private LocalDateTime timestamp;

    @Builder
    private ReviewUpdatedEvent(String reviewUuid,
                               String content,
                               int rating,
                               List<String> imageUrls,
                               LocalDateTime updatedAt
    ) {
        this.reviewUuid = reviewUuid;
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.updatedAt = updatedAt;
        this.timestamp = LocalDateTime.now();
    }
}