package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;

import java.util.List;

public  interface  ReviewImageMappingService {

    void registerReviewImages(String reviewUuid, List<ReviewImageRegisterRequestDto> reviewImageRegisterRequestDtos);

    List<String> getImageUrlsByReviewUuid(String reviewUuid);

    void markAsDeletedByReviewUuid(String reviewUuid);
}
