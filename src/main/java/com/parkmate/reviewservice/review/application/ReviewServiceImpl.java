package com.parkmate.reviewservice.review.application;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;
import com.parkmate.reviewservice.review.infrastructure.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public Review register(ReviewRegisterRequestDto reviewRegisterRequestDto) {

        // 예약 건당 리뷰 1건 → reservationCode 중복 체크
        boolean reviewExistsForReservation = reviewRepository.existsByReservationCode(reviewRegisterRequestDto.getReservationCode());
        if (reviewExistsForReservation) {
            throw new BaseException(ResponseStatus.REVIEW_ALREADY_EXISTS_FOR_PARKING_LOT);
        }


        // 동일 유저 + 동일 주차장 → 중복 리뷰 금지
        boolean reviewExistsForUserAndParkingLot = reviewRepository.existsByUserUuidAndParkingLotUuid(
                reviewRegisterRequestDto.getUserUuid(),
                reviewRegisterRequestDto.getParkingLotUuid()
        );

        if (reviewExistsForUserAndParkingLot) {
            throw new BaseException(ResponseStatus.REVIEW_ALREADY_EXISTS_FOR_PARKING_LOT);
        }

        Review review = Review.builder()
                .userUuid(reviewRegisterRequestDto.getUserUuid())
                .parkingLotUuid(reviewRegisterRequestDto.getParkingLotUuid())
                .reservationCode(reviewRegisterRequestDto.getReservationCode())
                .content(reviewRegisterRequestDto.getContent())
                .rating(reviewRegisterRequestDto.getRating())
                .build();

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    @Override
    public Review findReviewByUuid(String reviewUuid) {

        return reviewRepository.findByReviewUuidAndStatus(reviewUuid, ReviewStatus.ACTIVE)

                .orElseThrow(() -> new BaseException(ResponseStatus.RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public void update(ReviewUpdateRequestDto reviewUpdateRequestDto) {

        Review review = reviewRepository.findByReviewUuidAndUserUuidAndStatus(
                reviewUpdateRequestDto.getReviewUuid(),
                reviewUpdateRequestDto.getUserUuid(),
                ReviewStatus.ACTIVE
        ).orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_FORBIDDEN));

        review.updateReview(
                reviewUpdateRequestDto.getContent(),
                reviewUpdateRequestDto.getRating()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Review findActiveReviewByReviewUuidAndUserUuid(String reviewUuid, String userUuid) {

        Review review = reviewRepository.findByReviewUuidAndStatus(reviewUuid, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_NOT_FOUND));

        if (!review.getUserUuid().equals(userUuid)) {
            throw new BaseException(ResponseStatus.REVIEW_FORBIDDEN);
        }
        return review;
    }

    @Transactional
    public void softDeleteReview(String reviewUuid, String userUuid) {
        Review review = reviewRepository.findByReviewUuidAndUserUuidAndStatus(reviewUuid, userUuid, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ResponseStatus.REVIEW_ALREADY_DELETED));

        review.markAsDeleted();
    }

    @Override
    public Optional<Review> findByReservationCode(String reservationCode) {
        return reviewRepository.findByReservationCode(reservationCode);
    }
}
