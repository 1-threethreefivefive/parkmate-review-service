package com.parkmate.reviewservice.review.vo.request;

import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.Getter;
import java.util.List;

@Getter
public class ReviewUpdateRequestVo {

    private int rating;
    private String content;
    private List<ReviewImageRegisterRequestDto> imageMappings;
}