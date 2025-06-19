package com.parkmate.reviewservice.review.dto.response;

import com.parkmate.reviewservice.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class ReviewResponseDto {

    private final String reviewUuid;
    private final String userUuid;
    private final String parkingLotUuid;
    private final String paymentKey;
    private final String content;
    private final int rating;
    private final List<String> imageUrls;

    @Builder
    private ReviewResponseDto(String reviewUuid,
                              String userUuid,
                              String parkingLotUuid,
                              String paymentKey,
                              String content,
                              int rating,
                              List<String> imageUrls) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.paymentKey = paymentKey;
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }

    public static ReviewResponseDto from(Review review, List<String> imageUrls) {
        return ReviewResponseDto.builder()
                .reviewUuid(review.getReviewUuid())
                .userUuid(review.getUserUuid())
                .parkingLotUuid(review.getParkingLotUuid())
                //.paymentKey(review.getPaymentKey())
                .content(review.getContent())
                .rating(review.getRating())
                .imageUrls(imageUrls)
                .build();
    }
}
