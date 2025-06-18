package com.parkmate.reviewservice.review.application;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.generator.UUIDGenerator;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.kafka.event.CreateReviewEvent;
import com.parkmate.reviewservice.kafka.producer.CreateReviewProducer;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageMappingService reviewImageMappingService;
    private final CreateReviewProducer createReviewProducer;

    @Transactional
    @Override
    public Review register(ReviewRegisterRequestDto reviewRegisterRequestDto) {


//        // 결제 건 당 리뷰 1건 → paymentKey 중복 체크
//        boolean reviewExistsForPayment = reviewRepository.existsByPaymentKey(requestDto.getPaymentKey());
//        if (reviewExistsForPayment) {
//            throw new BaseException(ResponseStatus.REVIEW_ALREADY_EXISTS);
//        }

//        // 동일 유저 + 동일 주차장 → 중복 리뷰 금지
//        boolean reviewExistsForUserAndParkingLot = reviewRepository.existsByUserUuidAndParkingLotUuid(
//
//                reviewRegisterRequestDto.getUserUuid(),
//                reviewRegisterRequestDto.getParkingLotUuid()
//        );
//
//        if (reviewExistsForUserAndParkingLot) {
//            throw new BaseException(ResponseStatus.REVIEW_ALREADY_EXISTS_FOR_PARKING_LOT);
//        }

        Review review = Review.builder()
                .reviewId(UUIDGenerator.generateUUID())
                .userUuid(reviewRegisterRequestDto.getUserUuid())
                .parkingLotUuid(reviewRegisterRequestDto.getParkingLotUuid())
                // .paymentKey(requestDto.getPaymentKey())
                .content(reviewRegisterRequestDto.getContent())
                .rating(reviewRegisterRequestDto.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);

        if (reviewRegisterRequestDto.getImageMappings() != null &&
                !reviewRegisterRequestDto.getImageMappings().isEmpty()) {
            reviewImageMappingService.registerReviewImages(
                    savedReview.getId(),
                    reviewRegisterRequestDto.getImageMappings()
            );
        }

        CreateReviewEvent event = CreateReviewEvent.builder()
                .reviewUuid(savedReview.getReviewId())
                .userUuid(savedReview.getUserUuid())
                .parkingLotUuid(savedReview.getParkingLotUuid())
                .content(savedReview.getContent())
                .rating(savedReview.getRating())
                .imageUrls(
                        reviewRegisterRequestDto.getImageMappings().stream()
                                .map(ReviewImageRegisterRequestDto::getImageUrl)
                                .toList()
                )
                .likeCount(0)
                .dislikeCount(0)
                .createdAt(savedReview.getCreatedAt())
                .build();

        createReviewProducer.send(event);

        return savedReview;
    }

    @Transactional(readOnly = true)
    @Override
    public ReviewResponseDto findById(Long reviewId) {

        Review review = reviewRepository.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ResponseStatus.RESOURCE_NOT_FOUND));

        List<String> imageUrls = reviewImageMappingService.getImageUrlsByReviewId(reviewId);

        return ReviewResponseDto.from(review, imageUrls);
    }

    @Transactional
    @Override
    public void update(ReviewUpdateRequestDto reviewUpdateRequestDto) {

        Review review = reviewRepository.findByIdAndUserUuidAndStatus(
                        reviewUpdateRequestDto.getReviewId(),
                        reviewUpdateRequestDto.getUserUuid(),
                        ReviewStatus.ACTIVE
                )
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_FORBIDDEN));

        review.updateReview(
                reviewUpdateRequestDto.getContent(),
                reviewUpdateRequestDto.getRating()
        );

        if (reviewUpdateRequestDto.getImageMappings() != null && !reviewUpdateRequestDto.getImageMappings().isEmpty()) {
            reviewImageMappingService.markAsDeletedByReviewId(review.getId());
            reviewImageMappingService.registerReviewImages(review.getId(), reviewUpdateRequestDto.getImageMappings());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Review findEntityByIdAndUserUuid(Long reviewId, String userUuid) {

        return reviewRepository.findByIdAndUserUuidAndStatus(
                        reviewId,
                        userUuid,
                        ReviewStatus.ACTIVE
                )
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_FORBIDDEN));
    }
}