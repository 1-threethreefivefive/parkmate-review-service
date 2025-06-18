package com.parkmate.reviewservice.review.dto.request;

import com.parkmate.reviewservice.review.vo.request.ReviewRegisterRequestVo;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRegisterRequestDto {

    private String userUuid;
    private String parkingLotUuid;
    private String content;
    private Integer rating;
    private List<ReviewImageRegisterRequestDto> imageMappings;

    @Builder
    private ReviewRegisterRequestDto(String userUuid,
                                     String parkingLotUuid,
                                     String content,
                                     Integer rating,
                                     List<ReviewImageRegisterRequestDto> imageMappings) {
        this.userUuid = userUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.content = content;
        this.rating = rating;
        this.imageMappings = imageMappings;
    }

    public static ReviewRegisterRequestDto of(String userUuid, ReviewRegisterRequestVo reviewRegisterRequestVo) {

        return ReviewRegisterRequestDto.builder()
                .userUuid(userUuid)
                .parkingLotUuid(reviewRegisterRequestVo.getParkingLotUuid())
                .content(reviewRegisterRequestVo.getContent())
                .rating(reviewRegisterRequestVo.getRating())
                .imageMappings(reviewRegisterRequestVo.getImageMappings())
                .build();
    }
}