package com.parkmate.reviewservice.review.vo.response;

import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewResponseVo {

    private String reviewId;
    private String userUuid;
    private String parkingLotUuid;
    private String paymentKey;
    private String content;
    private int rating;
    private List<String> imageUrls;

    @Builder
    private ReviewResponseVo(String reviewId,
                             String userUuid,
                             String parkingLotUuid,
                             String paymentKey,
                             String content,
                             int rating,
                             List<String> imageUrls) {

        this.reviewId = reviewId;
        this.userUuid = userUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.paymentKey = paymentKey;
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }

    public static ReviewResponseVo from(ReviewResponseDto reviewResponseDto) {

        return ReviewResponseVo.builder()
                .reviewId(reviewResponseDto.getReviewId())
                .userUuid(reviewResponseDto.getUserUuid())
                .parkingLotUuid(reviewResponseDto.getParkingLotUuid())
                .paymentKey(reviewResponseDto.getPaymentKey())
                .content(reviewResponseDto.getContent())
                .rating(reviewResponseDto.getRating())
                .imageUrls(reviewResponseDto.getImageUrls())
                .build();
    }
}