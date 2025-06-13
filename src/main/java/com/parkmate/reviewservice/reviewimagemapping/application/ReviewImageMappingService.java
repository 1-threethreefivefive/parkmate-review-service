package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import com.parkmate.reviewservice.reviewimagemapping.dto.response.ReviewImageMappingResponseDto;
import java.util.List;

public  interface  ReviewImageMappingService {

    List<ReviewImageMappingResponseDto> registerReviewImages(Long reviewId, List<ReviewImageRegisterRequestDto> reviewImageRegisterRequestDtos);

    List<String> getImageUrlsByReviewId(Long reviewId);

    void markAsDeletedByReviewId(Long reviewId);
}
