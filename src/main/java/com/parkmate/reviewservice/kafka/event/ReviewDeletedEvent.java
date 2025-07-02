package com.parkmate.reviewservice.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewDeletedEvent {
    private String reviewUuid;
    private LocalDateTime deletedAt;

    @Builder
    public ReviewDeletedEvent(String reviewUuid, LocalDateTime deletedAt) {
        this.reviewUuid = reviewUuid;
        this.deletedAt = deletedAt;
    }
}
