package com.parkmate.reviewservice.review.infrastructure;

import com.parkmate.reviewservice.review.domain.Review;
import com.parkmate.reviewservice.review.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewIdAndUserUuidAndStatus(String reviewId, String userUuid, ReviewStatus status);

    Optional<Review> findByReviewIdAndStatus(String reviewId, ReviewStatus status);
}
