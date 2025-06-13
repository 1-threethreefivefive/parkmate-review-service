package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMappingStatus;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import com.parkmate.reviewservice.reviewimagemapping.dto.response.ReviewImageMappingResponseDto;
import com.parkmate.reviewservice.reviewimagemapping.infrastructure.ReviewImageMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageMappingServiceImpl implements ReviewImageMappingService {

    private final ReviewImageMappingRepository reviewImageMappingRepository;

    @Transactional
    @Override
    public List<ReviewImageMappingResponseDto> registerReviewImages(Long reviewId, List<ReviewImageRegisterRequestDto> reviewImageRegisterRequestDtos) {

        if (reviewImageRegisterRequestDtos == null || reviewImageRegisterRequestDtos.isEmpty()) {
            return Collections.emptyList();
        }

        markAsDeletedByReviewId(reviewId);

        List<ReviewImageMapping> reviewImageMappingList = new ArrayList<>();

        int imageIndexCounter = 0;

        for (ReviewImageRegisterRequestDto reviewImageRegisterRequestDto : reviewImageRegisterRequestDtos) {

            Integer imageIndex = "IMAGE".equals(reviewImageRegisterRequestDto.getType()) ? imageIndexCounter++ : null;

            ReviewImageMapping imageMapping = ReviewImageMapping.builder()
                    .reviewId(reviewId)
                    .imageUrl(reviewImageRegisterRequestDto.getImageUrl())
                    .type(reviewImageRegisterRequestDto.getType())
                    .imageIndex(imageIndex)
                    .build();

            imageMapping.markAsActive();

            reviewImageMappingList.add(imageMapping);
        }

        List<ReviewImageMapping> savedMappings = reviewImageMappingRepository.saveAll(reviewImageMappingList);

        return savedMappings.stream()
                .map(ReviewImageMappingResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getImageUrlsByReviewId(Long reviewId) {

        return reviewImageMappingRepository.findAllByReviewIdAndStatusOrderByImageIndex(reviewId, ReviewImageMappingStatus.ACTIVE)
                .stream()
                .map(ReviewImageMapping::getImageUrl)
                .toList();
    }

    @Transactional
    @Override
    public void markAsDeletedByReviewId(Long reviewId) {

        List<ReviewImageMapping> images = reviewImageMappingRepository.findAllByReviewId(reviewId);

        for (ReviewImageMapping image : images) {
            image.markAsDeleted();
        }
    }
}