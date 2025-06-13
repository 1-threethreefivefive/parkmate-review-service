package com.parkmate.reviewservice.review.application;

import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;

public interface ReviewService {

    Review register(ReviewRegisterRequestDto requestDto);

    void update(ReviewUpdateRequestDto requestDto);

    Review findEntityByIdAndUserUuid(Long reviewId, String userUuid);

    ReviewResponseDto findById(Long reviewId);
}
