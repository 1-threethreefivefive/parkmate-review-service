package com.parkmate.reviewservice.reviewreactions.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_reactions",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_reactions_review_user",
                columnNames = {"reviewUuid", "userUuid"}
        )
)
public class ReviewReactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("리뷰 UUID")
    @Column(length = 36, nullable = false)
    private String reviewUuid;

    @Comment("사용자 UUID")
    @Column(length = 36, nullable = false)
    private String userUuid;

    @Comment("리액션 타입")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType = ReactionType.NONE;

    @Builder
    private ReviewReactions(Long id,
                            String reviewUuid,
                            String userUuid,
                            ReactionType reactionType) {
        this.id = id;
        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.reactionType = reactionType;
    }

    public void updateReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}

