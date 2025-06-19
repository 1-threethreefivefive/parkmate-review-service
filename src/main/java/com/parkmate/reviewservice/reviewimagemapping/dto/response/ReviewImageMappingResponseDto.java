package com.parkmate.reviewservice.reviewimagemapping.dto.response;

import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImageMappingResponseDto {

    private String reviewUuid;
    private String imageUrl;
    private String type;

    @Builder
    private ReviewImageMappingResponseDto(String reviewUuid,
                                          String imageUrl,
                                          String type) {
        this.reviewUuid = reviewUuid;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public static ReviewImageMappingResponseDto from(ReviewImageMapping reviewImageMapping) {

        return ReviewImageMappingResponseDto.builder()
                .reviewUuid(reviewImageMapping.getReviewUuid())
                .imageUrl(reviewImageMapping.getImageUrl())
                .type(reviewImageMapping.getType())
                .build();
    }
}