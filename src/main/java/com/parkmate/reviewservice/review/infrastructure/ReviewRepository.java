package com.parkmate.reviewservice.review.infrastructure;

import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewUuidAndUserUuidAndStatus(String reviewUuid, String userUuid, ReviewStatus status);

    Optional<Review> findByReviewUuidAndStatus(String reviewUuid, ReviewStatus status);

    boolean existsByReservationCode(String reservationCode);


    boolean existsByUserUuidAndParkingLotUuid(String userUuid, String parkingLotUuid);

    Optional<Review> findByReservationCode(String reservationCode);

}
