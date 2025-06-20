package com.parkmate.reviewservice.reviewreactions.application;

import com.parkmate.reviewservice.kafka.event.ReactionUpdatedEvent;
import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import com.parkmate.reviewservice.reviewreactions.domain.ReviewReactions;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionGetRequestDto;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionUpsertRequestDto;
import com.parkmate.reviewservice.reviewreactions.infrastructure.ReviewReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewReactionsServiceImpl implements ReviewReactionsService {

    private final ReviewReactionsRepository reviewReactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void addReaction(ReviewReactionUpsertRequestDto reviewReactionUpsertRequestDto) {

        Optional<ReviewReactions> existing = reviewReactionRepository.findByReviewUuidAndUserUuid(
                reviewReactionUpsertRequestDto.getReviewUuid(),
                reviewReactionUpsertRequestDto.getUserUuid()
        );

        ReactionType previousReactionType = ReactionType.NONE;

        if (existing.isEmpty()) {
            reviewReactionRepository.save(reviewReactionUpsertRequestDto.toEntity());
        } else {
            ReviewReactions reaction = existing.get();
            previousReactionType = reaction.getReactionType();
            reaction.updateReactionType(reviewReactionUpsertRequestDto.getReactionType());

            reviewReactionRepository.save(reaction);
        }

        eventPublisher.publishEvent(
                    ReactionUpdatedEvent.builder()
                        .reviewUuid(reviewReactionUpsertRequestDto.getReviewUuid())
                        .userUuid(reviewReactionUpsertRequestDto.getUserUuid())
                        .reactionType(reviewReactionUpsertRequestDto.getReactionType())
                        .previousReactionType(previousReactionType)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ReactionType getReaction(ReviewReactionGetRequestDto reviewReactionGetRequestDto) {

        return reviewReactionRepository.findReactionTypeByReviewUuidAndUserUuid(

                reviewReactionGetRequestDto.getReviewUuid(),
                reviewReactionGetRequestDto.getUserUuid()
        ).orElse(ReactionType.NONE);
    }
}
