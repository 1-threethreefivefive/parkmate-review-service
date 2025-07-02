package com.parkmate.reviewservice.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewExistenceResponseDto {

    private final String reviewUuid;

    @Builder
    private ReviewExistenceResponseDto(String reviewUuid) {
        this.reviewUuid = reviewUuid;
    }

    public static ReviewExistenceResponseDto from(String reviewUuid) {
        return ReviewExistenceResponseDto.builder()
                .reviewUuid(reviewUuid)
                .build();
    }
}