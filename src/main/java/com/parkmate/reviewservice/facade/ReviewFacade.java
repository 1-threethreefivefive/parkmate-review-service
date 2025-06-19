package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.infrastructure.ReviewRepository;
import com.parkmate.reviewservice.reviewimagemapping.application.ReviewImageMappingService;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;
    private final ReviewImageMappingService reviewImageMappingService;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void registerReview(ReviewRegisterRequest reviewRegisterRequest) {
        Review review = reviewService.register(reviewRegisterRequest.getReview());

        List<ReviewImageRegisterRequestDto> imageMappings = reviewRegisterRequest.getImageMappings();

        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.validateImageConstraints(imageMappings);
            reviewImageMappingService.registerReviewImages(review.getReviewUuid(), imageMappings);
        }
    }

    @Transactional
    public void updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) {

        reviewService.update(reviewUpdateRequestDto);

        List<ReviewImageRegisterRequestDto> imageMappings = reviewUpdateRequestDto.getImageMappings();

        reviewImageMappingService.markAsDeletedByReviewId(reviewUpdateRequestDto.getReviewUuid());

        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(reviewUpdateRequestDto.getReviewUuid(), imageMappings);
        }
    }

    @Transactional
    public void softDeleteReview(String reviewUuid, String userUuid) {

        reviewService.softDeleteReview(reviewUuid, userUuid);
    }
}