package com.parkmate.reviewservice.reviewimagemapping.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImageRegisterRequestDto {

    private String imageUrl;
    private String type;

    @Builder
    private ReviewImageRegisterRequestDto(String imageUrl,
                                          String type) {

        this.imageUrl = imageUrl;
        this.type = type;
    }
}