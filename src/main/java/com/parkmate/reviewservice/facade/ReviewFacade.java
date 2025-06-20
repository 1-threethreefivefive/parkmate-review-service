package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.kafka.event.CreateReviewEvent;
import com.parkmate.reviewservice.kafka.producer.ReviewProducer;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
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
    private final ReviewProducer reviewProducer;

    @Transactional
    public void registerReview(ReviewRegisterRequest reviewRegisterRequest) {
        // 1. 리뷰 등록
        Review review = reviewService.register(reviewRegisterRequest.getReview());

        // 2. 이미지 등록 (존재할 경우)
        List<ReviewImageRegisterRequestDto> imageMappings = reviewRegisterRequest.getImageMappings();
        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(review.getReviewUuid(), imageMappings);
        }

        // 3. Kafka 이벤트 발행
        CreateReviewEvent event = CreateReviewEvent.builder()
                .reviewUuid(review.getReviewUuid())
                .userUuid(review.getUserUuid())
                .parkingLotUuid(review.getParkingLotUuid())
                .content(review.getContent())
                .rating(review.getRating())
                .imageUrls(imageMappings == null
                        ? List.of()
                        : imageMappings.stream()
                        .map(ReviewImageRegisterRequestDto::getImageUrl)
                        .toList()
                )
                .build();

        reviewProducer.sendCreateReviewEvent(event);
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewWithImages(String reviewUuid) {

        Review review = reviewService.findReviewByUuid(reviewUuid);
        List<String> imageUrls = reviewImageMappingService.getImageUrlsByReviewUuid(reviewUuid);

        return ReviewResponseDto.from(review, imageUrls);
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