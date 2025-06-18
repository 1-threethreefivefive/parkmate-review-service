package com.parkmate.reviewservice.review.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDeleteRequestDto {

    private String reviewId;
    private String userUuid;

    @Builder
    private ReviewDeleteRequestDto(String reviewId, String userUuid) {
        this.reviewId = reviewId;
        this.userUuid = userUuid;
    }
}