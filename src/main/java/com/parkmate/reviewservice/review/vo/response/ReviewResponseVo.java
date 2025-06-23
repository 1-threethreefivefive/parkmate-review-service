package com.parkmate.reviewservice.review.vo.response;

import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewResponseVo {

    private String reviewUuid;

    private String userUuid;
    private String content;
    private int rating;
    private List<ImageUrlVo> imageUrls;

    @Builder
    private ReviewResponseVo(String userUuid,
                             String content,
                             int rating,
                             List<ImageUrlVo> imageUrls) {
      
        this.userUuid = userUuid;
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }

    public static ReviewResponseVo from(ReviewResponseDto dto) {
        return ReviewResponseVo.builder()

                .userUuid(dto.getUserUuid())
                .content(dto.getContent())
                .rating(dto.getRating())
                .imageUrls(ImageUrlVo.from(dto.getImageUrls()))
                .build();
    }
}