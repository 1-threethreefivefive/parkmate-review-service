package com.parkmate.reviewservice.reviewimagemapping.dto.response;

import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImageMappingResponseDto {

    private Long reviewId;
    private String imageUrl;
    private String type;

    @Builder
    private ReviewImageMappingResponseDto(Long reviewId,
                                          String imageUrl,
                                          String type) {
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public static ReviewImageMappingResponseDto from(ReviewImageMapping reviewImageMapping) {

        return ReviewImageMappingResponseDto.builder()
                .reviewId(reviewImageMapping.getReviewId())
                .imageUrl(reviewImageMapping.getImageUrl())
                .type(reviewImageMapping.getType())
                .build();
    }
}