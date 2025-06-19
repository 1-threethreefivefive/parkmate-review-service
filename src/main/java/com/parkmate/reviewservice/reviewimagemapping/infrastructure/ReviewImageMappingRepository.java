package com.parkmate.reviewservice.reviewimagemapping.infrastructure;

import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMappingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewImageMappingRepository extends JpaRepository<ReviewImageMapping, Long> {

    List<ReviewImageMapping> findAllByReviewUuidAndStatusOrderByImageIndex(String reviewUuid, ReviewImageMappingStatus status);

    List<ReviewImageMapping> findAllByReviewUuid(String reviewUuid);
}