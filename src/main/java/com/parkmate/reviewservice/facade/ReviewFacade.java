package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.infrastructure.ReviewRepository;
import com.parkmate.reviewservice.reviewimagemapping.application.ReviewImageMappingService;
import com.parkmate.reviewservice.reviewimagemapping.domain.type.MediaType;
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

        List<ReviewImageRegisterRequestDto> imageMappings = reviewRegisterRequest.getImageMappings();

        validateImageConstraints(imageMappings);

        Review review = reviewService.register(reviewRegisterRequest.getReview());

        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(review.getReviewId(), imageMappings);
        }
    }

    /**
     * 이미지 등록 조건 검증
     */
    private void validateImageConstraints(List<ReviewImageRegisterRequestDto> imageMappings) {
        if (imageMappings == null || imageMappings.isEmpty()) return;

        long imageCount = imageMappings.stream()
                .filter(dto -> MediaType.IMAGE.name().equals(dto.getType()))
                .count();

        long videoCount = imageMappings.stream()
                .filter(dto -> MediaType.VIDEO.name().equals(dto.getType()))
                .count();

        if (imageCount > 5) {
            throw new BaseException(ResponseStatus.REVIEW_IMAGE_LIMIT_EXCEEDED);
        }

        if (videoCount > 1) {
            throw new BaseException(ResponseStatus.REVIEW_VIDEO_LIMIT_EXCEEDED);
        }
    }

    @Transactional
    public void updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) {

        reviewService.update(reviewUpdateRequestDto);

        List<ReviewImageRegisterRequestDto> imageMappings = reviewUpdateRequestDto.getImageMappings();

        // 기존 이미지 전부 soft delete
        reviewImageMappingService.markAsDeletedByReviewId(reviewUpdateRequestDto.getReviewId());

        // 새 이미지가 있다면 등록
        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(reviewUpdateRequestDto.getReviewId(), imageMappings);
        }
    }

    /**
     * 리뷰 논리 삭제
     */
    @Transactional
    public void softDeleteReview(String reviewId, String userUuid) {
        Review review = reviewRepository.findByReviewIdAndUserUuidAndStatus(reviewId, userUuid, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_ALREADY_DELETED));

        review.markAsDeleted();
    }
}