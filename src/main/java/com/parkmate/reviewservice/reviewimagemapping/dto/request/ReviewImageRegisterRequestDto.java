package com.parkmate.reviewservice.reviewimagemapping.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImageRegisterRequestDto {

    private Long reviewId;
    private String imageUrl;
    private String type;
    private Integer imageIndex;

    @Builder
    private ReviewImageRegisterRequestDto(Long reviewId, String imageUrl, String type,Integer imageIndex) {
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.type = type;
        this.imageIndex = imageIndex;
    }
}