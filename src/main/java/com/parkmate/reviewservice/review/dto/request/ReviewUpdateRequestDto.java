package com.parkmate.reviewservice.review.dto.request;

import com.parkmate.reviewservice.review.vo.request.ReviewUpdateRequestVo;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {

    private String reviewId;
    private String userUuid;
    private int rating;
    private String content;
    private List<ReviewImageRegisterRequestDto> imageMappings;

    @Builder
    private ReviewUpdateRequestDto(String reviewId,
                                   String userUuid,
                                   int  rating,
                                   String content,
                                   List<ReviewImageRegisterRequestDto> imageMappings) {
        this.reviewId = reviewId;
        this.userUuid = userUuid;
        this.rating = rating;
        this.content = content;
        this.imageMappings = imageMappings;
    }

    public static ReviewUpdateRequestDto of(String reviewId,
                                            String userUuid,
                                            ReviewUpdateRequestVo reviewUpdateRequestVo) {

        return ReviewUpdateRequestDto.builder()
                .reviewId(reviewId)
                .userUuid(userUuid)
                .rating(reviewUpdateRequestVo.getRating())
                .content(reviewUpdateRequestVo.getContent())
                .imageMappings(reviewUpdateRequestVo.getImageMappings())
                .build();
    }
}
