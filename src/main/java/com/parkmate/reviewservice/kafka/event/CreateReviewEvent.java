package com.parkmate.reviewservice.kafka.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewEvent {

    private String reviewUuid;
    private String userUuid;
    private String parkingLotUuid;
    private String content;
    private int rating;
    private List<String> imageUrls;
    private int likeCount;
    private int dislikeCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private CreateReviewEvent(
            String reviewUuid,
            String userUuid,
            String parkingLotUuid,
            String content,
            int rating,
            List<String> imageUrls,
            int likeCount,
            int dislikeCount,
            LocalDateTime createdAt
    ) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
