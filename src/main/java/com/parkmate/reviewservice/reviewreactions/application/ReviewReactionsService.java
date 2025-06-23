package com.parkmate.reviewservice.reviewreactions.application;

import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionGetRequestDto;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionUpsertRequestDto;

public interface ReviewReactionsService {

    void addReaction(ReviewReactionUpsertRequestDto reviewReactionUpsertRequestDto);

    ReactionType getReaction(ReviewReactionGetRequestDto reviewReactionGetRequestDto);
}
