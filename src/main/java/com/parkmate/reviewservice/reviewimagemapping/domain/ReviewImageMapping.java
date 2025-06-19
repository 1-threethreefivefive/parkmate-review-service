package com.parkmate.reviewservice.reviewimagemapping.domain;

import com.parkmate.reviewservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_image_mapping")
public class ReviewImageMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("리뷰 ID (FK)")
    @Column(name = "review_uuid", nullable = false)
    private String reviewUuid;

    @Comment("이미지/동영상 URL")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Comment("타입 (IMAGE / VIDEO)")
    @Column(nullable = false, length = 50)
    private String type;

    @Comment("이미지 인덱스 (0~4), 비디오 0")
    @Column(name = "image_index")
    private Integer imageIndex;

    @Comment("삭제 일시 (Soft Delete 적용)")
    @Column
    private LocalDateTime deletedAt;

    @Comment("이미지 상태 (ACTIVE / DELETED)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewImageMappingStatus status;

    @Builder
    private ReviewImageMapping(String reviewUuid,
                               String imageUrl,
                               String type,
                               Integer imageIndex) {

        this.reviewUuid = reviewUuid;
        this.imageUrl = imageUrl;
        this.type = type;
        this.imageIndex = imageIndex;
        this.status = ReviewImageMappingStatus.ACTIVE;
        this.deletedAt = null;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.status = ReviewImageMappingStatus.DELETED;
    }

    public void markAsActive() {
        this.status = ReviewImageMappingStatus.ACTIVE;
        this.deletedAt = null;
    }

    public boolean isActive() {
        return this.status == ReviewImageMappingStatus.ACTIVE;
    }
}