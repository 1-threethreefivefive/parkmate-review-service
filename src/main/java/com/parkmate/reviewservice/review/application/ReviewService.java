package com.parkmate.reviewservice.review.application;

import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;

import java.util.Optional;

public interface ReviewService {

    Review register(ReviewRegisterRequestDto requestDto);

    void update(ReviewUpdateRequestDto requestDto);

    Review findReviewByUuid(String reviewUuid);

    Review findActiveReviewByReviewUuidAndUserUuid(String reviewUuid, String userUuid);

    void softDeleteReview(String reviewUuid, String userUuid);

    Optional<Review> findByReservationCode(String ReservationCode);
}
