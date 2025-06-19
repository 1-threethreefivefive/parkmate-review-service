package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;
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

            reviewImageMappingService.registerReviewImages(review.getReviewUuid(), imageMappings);
        }
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewWithImages(String reviewUuid) {

        Review review = reviewService.findReviewByUuid(reviewUuid);
        List<String> imageUrls = reviewImageMappingService.getImageUrlsByReviewUuid(reviewUuid);

        return ReviewResponseDto.from(review, imageUrls);
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
        reviewImageMappingService.markAsDeletedByReviewUuid(reviewUpdateRequestDto.getReviewUuid());


        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(reviewUpdateRequestDto.getReviewUuid(), imageMappings);
        }
    }

    @Transactional
    public void softDeleteReview(String reviewUuid, String userUuid) {

        Review review = reviewRepository.findByReviewUuidAndUserUuidAndStatus(reviewUuid, userUuid, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_ALREADY_DELETED));


        reviewService.softDeleteReview(reviewUuid, userUuid);
    }
}