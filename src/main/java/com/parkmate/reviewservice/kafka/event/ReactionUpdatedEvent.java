package com.parkmate.reviewservice.kafka.event;

import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReactionUpdatedEvent {

    private String reviewUuid;
    private String userUuid;
    private ReactionType reactionType;
    private ReactionType previousReactionType;
    private LocalDateTime timestamp;

    @Builder
    private ReactionUpdatedEvent(String reviewUuid,
                                 String userUuid,
                                 ReactionType reactionType,
                                 ReactionType previousReactionType) {

        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.reactionType = reactionType;
        this.previousReactionType = previousReactionType;
        this.timestamp = LocalDateTime.now();
    }
}