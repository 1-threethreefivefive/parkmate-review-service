package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.domain.Review;
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

    @Transactional
    public void registerReview(ReviewRegisterRequest reviewRegisterRequest) {

        List<ReviewImageRegisterRequestDto> imageMappings = reviewRegisterRequest.getImageMappings();

        if (imageMappings != null && !imageMappings.isEmpty()) {

            long imageCount = imageMappings.stream()
                    .filter(dto -> "IMAGE".equals(dto.getType()))
                    .count();

            long videoCount = imageMappings.stream()
                    .filter(dto -> "VIDEO".equals(dto.getType()))
                    .count();

            if (imageCount > 5) {
                throw new BaseException(ResponseStatus.REVIEW_IMAGE_LIMIT_EXCEEDED);
            }

            if (videoCount > 1) {
                throw new BaseException(ResponseStatus.REVIEW_VIDEO_LIMIT_EXCEEDED);
            }
        }

        Review review = reviewService.register(reviewRegisterRequest.getReview());

        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(review.getId(), imageMappings);
        }
    }

    @Transactional
    public void softDeleteReview(Long reviewId, String userUuid) {

        Review review = reviewService.findEntityByIdAndUserUuid(reviewId, userUuid);

        if (!review.isActive()) {
            throw new BaseException(ResponseStatus.REVIEW_ALREADY_DELETED);
        }

        review.markAsDeleted();
    }
}