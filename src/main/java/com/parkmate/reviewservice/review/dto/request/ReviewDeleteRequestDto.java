package com.parkmate.reviewservice.review.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDeleteRequestDto {

    private String reviewUuid;
    private String userUuid;

    @Builder
    private ReviewDeleteRequestDto(String reviewUuid, String userUuid) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
    }
}