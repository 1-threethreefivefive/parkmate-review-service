package com.parkmate.reviewservice.reviewreactions.infrastructure;


import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import com.parkmate.reviewservice.reviewreactions.domain.ReviewReactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ReviewReactionsRepository extends JpaRepository<ReviewReactions, Long> {

    Optional<ReviewReactions> findByReviewUuidAndUserUuid(String reviewUuid, String userUuid);

    @Query("SELECT rr.reactionType " +
            "FROM ReviewReactions rr " +
            "WHERE rr.reviewUuid = :reviewUuid AND rr.userUuid = :userUuid")
    Optional<ReactionType> findReactionTypeByReviewUuidAndUserUuid(String reviewUuid, String userUuid);
}
