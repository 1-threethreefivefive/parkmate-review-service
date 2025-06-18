package com.parkmate.reviewservice.facade.dto;

import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterRequest {

    private ReviewRegisterRequestDto review;
    private List<ReviewImageRegisterRequestDto> imageMappings;
}
