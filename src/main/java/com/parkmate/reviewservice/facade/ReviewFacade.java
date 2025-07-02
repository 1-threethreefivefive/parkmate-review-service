package com.parkmate.reviewservice.facade;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.kafka.event.CreateReviewEvent;
import com.parkmate.reviewservice.kafka.event.ReviewDeletedEvent;
import com.parkmate.reviewservice.kafka.event.ReviewUpdatedEvent;
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

        Review review = reviewService.register(reviewRegisterRequest.getReview());

        List<ReviewImageRegisterRequestDto> imageMappings = reviewRegisterRequest.getImageMappings();
        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(review.getReviewUuid(), imageMappings);
        }

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
        reviewImageMappingService.markAsDeletedByReviewUuid(reviewUpdateRequestDto.getReviewUuid());

        if (imageMappings != null && !imageMappings.isEmpty()) {
            reviewImageMappingService.registerReviewImages(reviewUpdateRequestDto.getReviewUuid(), imageMappings);
        }

        Review updatedReview = reviewService.findReviewByUuid(reviewUpdateRequestDto.getReviewUuid());
        List<String> imageUrls = reviewImageMappingService.getImageUrlsByReviewUuid(reviewUpdateRequestDto.getReviewUuid());

        ReviewUpdatedEvent event = ReviewUpdatedEvent.builder()
                .reviewUuid(updatedReview.getReviewUuid())
                .content(updatedReview.getContent())
                .rating(updatedReview.getRating())
                .imageUrls(imageUrls)
                .updatedAt(updatedReview.getUpdatedAt())
                .build();

        reviewProducer.sendReviewUpdatedEvent(event);
    }

    @Transactional
    public void softDeleteReview(String reviewUuid, String userUuid) {

        Review review = reviewRepository.findByReviewUuidAndUserUuidAndStatus(
                reviewUuid, userUuid, ReviewStatus.ACTIVE
        ).orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_ALREADY_DELETED));

        review.markAsDeleted();

        ReviewDeletedEvent event = ReviewDeletedEvent.builder()
                .reviewUuid(review.getReviewUuid())
                .deletedAt(review.getUpdatedAt())
                .build();

        reviewProducer.sendReviewDeletedEvent(event);
    }
}