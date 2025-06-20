package com.parkmate.reviewservice.reviewreactions.dto.request;

import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import com.parkmate.reviewservice.reviewreactions.domain.ReviewReactions;
import com.parkmate.reviewservice.reviewreactions.vo.request.ReviewReactionUpsertRequestVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReactionUpsertRequestDto {

    private String reviewUuid;
    private String userUuid;
    private ReactionType reactionType;

    @Builder
    private ReviewReactionUpsertRequestDto(String reviewUuid,
                                           String userUuid,
                                           ReactionType reactionType) {
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.reactionType = reactionType;
    }

    public static ReviewReactionUpsertRequestDto of(String reviewUuid,
                                                    String userUuid,
                                                    ReviewReactionUpsertRequestVo reviewReactionUpsertRequestVo) {
        return ReviewReactionUpsertRequestDto.builder()
                .reviewUuid(reviewUuid)
                .userUuid(userUuid)
                .reactionType(reviewReactionUpsertRequestVo.getReactionType())
                .build();
    }

    public ReviewReactions toEntity() {
        return ReviewReactions.builder()
                .reviewUuid(reviewUuid)
                .userUuid(userUuid)
                .reactionType(reactionType)
                .build();
    }
}
