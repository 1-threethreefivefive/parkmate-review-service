package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;

import java.util.List;

public  interface  ReviewImageMappingService {

    void registerReviewImages(String reviewId, List<ReviewImageRegisterRequestDto> reviewImageRegisterRequestDtos);

    List<String> getImageUrlsByReviewId(String reviewId);

    void markAsDeletedByReviewId(String reviewId);
}
