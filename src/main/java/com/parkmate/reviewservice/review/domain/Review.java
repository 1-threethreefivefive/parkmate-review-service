package com.parkmate.reviewservice.review.domain;

import com.parkmate.reviewservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("리뷰 UUID - 외부 식별자")
    @Column(nullable = false, unique = true, length = 36)
    private String reviewUuid;

    @Column(nullable = false, length = 36)
    @Comment("회원 UUID")
    private String userUuid;

    @Column(nullable = false, length = 36)
    @Comment("주차장 UUID")
    private String parkingLotUuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("리뷰 내용")
    private String content;

    @Column(nullable = false)
    @Comment("평점")
    private int rating;

    @Comment("삭제 일시")
    @Column
    private LocalDateTime deletedAt;

    @Comment("리뷰 상태 (ACTIVE / DELETED)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status;

    @Builder
    private Review(String reviewUuid,
                   String userUuid,
                   String parkingLotUuid,
                   String content,
                   int rating) {

        this.reviewUuid = reviewUuid;
        this.userUuid = userUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.content = content;
        this.rating = rating;
        this.deletedAt = null;
        this.status = ReviewStatus.ACTIVE;
    }

    public void updateReview(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.status = ReviewStatus.DELETED;
    }

    @PrePersist
    protected void onPrePersist() {
        if (this.reviewUuid == null) {
            this.reviewUuid = java.util.UUID.randomUUID().toString();
        }
    }
    public boolean isActive() {
        return this.status == ReviewStatus.ACTIVE;
    }
}