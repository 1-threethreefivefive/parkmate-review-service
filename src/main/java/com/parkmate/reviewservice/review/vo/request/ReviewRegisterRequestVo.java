package com.parkmate.reviewservice.review.vo.request;

import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRegisterRequestVo {

    private String parkingLotUuid;
    private int  rating;
    private String content;
    private List<ReviewImageRegisterRequestDto> imageMappings;
}