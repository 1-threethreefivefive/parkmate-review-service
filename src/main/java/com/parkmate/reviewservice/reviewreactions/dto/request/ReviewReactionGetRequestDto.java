package com.parkmate.reviewservice.reviewreactions.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReactionGetRequestDto {

    private String reviewUuid;
    private String userUuid;

    @Builder
    private ReviewReactionGetRequestDto(String reviewUuid, String userUuid) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
    }

    public static ReviewReactionGetRequestDto of(String reviewUuid, String userUuid) {
        return ReviewReactionGetRequestDto.builder()
                .reviewUuid(reviewUuid)
                .userUuid(userUuid)
                .build();
    }
}