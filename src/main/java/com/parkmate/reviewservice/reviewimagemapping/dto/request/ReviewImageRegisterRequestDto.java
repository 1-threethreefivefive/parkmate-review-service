package com.parkmate.reviewservice.reviewimagemapping.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImageRegisterRequestDto {

    private String reviewUuid;
    private String imageUrl;
    private String type;

    @Builder
    private ReviewImageRegisterRequestDto(String reviewUuid,
                                          String imageUrl,
                                          String type) {
        this.reviewUuid = reviewUuid;
        this.imageUrl = imageUrl;
        this.type = type;
    }
}