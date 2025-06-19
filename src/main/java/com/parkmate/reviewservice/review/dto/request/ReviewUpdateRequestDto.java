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

    private String reviewUuid;
    private String userUuid;
    private int rating;
    private String content;
    private List<ReviewImageRegisterRequestDto> imageMappings;

    @Builder
    private ReviewUpdateRequestDto(String reviewUuid,
                                   String userUuid,
                                   int  rating,
                                   String content,
                                   List<ReviewImageRegisterRequestDto> imageMappings) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.rating = rating;
        this.content = content;
        this.imageMappings = imageMappings;
    }

    public static ReviewUpdateRequestDto of(String reviewUuid,
                                            String userUuid,
                                            ReviewUpdateRequestVo reviewUpdateRequestVo) {

        return ReviewUpdateRequestDto.builder()
                .reviewUuid(reviewUuid)
                .userUuid(userUuid)
                .rating(reviewUpdateRequestVo.getRating())
                .content(reviewUpdateRequestVo.getContent())
                .imageMappings(reviewUpdateRequestVo.getImageMappings())
                .build();
    }
}
