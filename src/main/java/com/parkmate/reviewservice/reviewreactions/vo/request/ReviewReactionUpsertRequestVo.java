package com.parkmate.reviewservice.reviewreactions.vo.request;

import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReactionUpsertRequestVo {

    private ReactionType reactionType;

    @Builder
    private ReviewReactionUpsertRequestVo(ReactionType reactionType) {

        this.reactionType = reactionType;
    }
}
