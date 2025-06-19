package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import com.parkmate.reviewservice.reviewimagemapping.domain.type.MediaType;
import com.parkmate.reviewservice.reviewimagemapping.dto.request.ReviewImageRegisterRequestDto;
import com.parkmate.reviewservice.reviewimagemapping.infrastructure.ReviewImageMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageMappingServiceImpl implements ReviewImageMappingService {

    private final ReviewImageMappingRepository reviewImageMappingRepository;

    @Transactional
    @Override
    public void registerReviewImages(String reviewUuid, List<ReviewImageRegisterRequestDto> reviewImageRegisterRequestDtos) {

        if (imageDtos == null || imageDtos.isEmpty()) return;

        // 1. imageUrl이 null이거나 빈 문자열인 경우는 필터링
        List<ReviewImageRegisterRequestDto> validDtos = imageDtos.stream()
                .filter(dto -> dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty())
                .toList();


        markAsDeletedByReviewUuid(reviewUuid);

        List<ReviewImageMapping> reviewImageMappingList = new ArrayList<>();
        int imageIndexCounter = 0;

        for (ReviewImageRegisterRequestDto dto : reviewImageRegisterRequestDtos) {

            Integer imageIndex = "IMAGE".equals(dto.getType()) ? imageIndexCounter++ : null;

            ReviewImageMapping imageMapping = ReviewImageMapping.builder()
                    .reviewUuid(reviewUuid)
                    .imageUrl(dto.getImageUrl())
                    .type(dto.getType())
                    .imageIndex(imageIndex)
                    .build();

            imageMapping.markAsActive();
            reviewImageMappingList.add(imageMapping);
        }

        if (!mappingsToSave.isEmpty()) {
            reviewImageMappingRepository.saveAll(mappingsToSave);
        }
    }

    @Transactional
    @Override
    public List<String> getImageUrlsByReviewUuid(String reviewUuid) {

        return reviewImageMappingRepository.findAllByReviewUuidAndStatusOrderByImageIndex(reviewUuid, ReviewImageMappingStatus.ACTIVE)
                .stream()
                .map(ReviewImageMapping::getImageUrl)
                .toList();
    }

    @Transactional
    @Override
    public void markAsDeletedByReviewUuid(String reviewUuid) {
        List<ReviewImageMapping> images = reviewImageMappingRepository.findAllByReviewUuid(reviewUuid);
        for (ReviewImageMapping image : images) {
            image.markAsDeleted();
        }
    }

    @Transactional
    @Override
    public void markAsDeletedByReviewId(String reviewUuid) {
        List<ReviewImageMapping> images = reviewImageMappingRepository.findAllByReviewUuid(reviewUuid);
        images.forEach(ReviewImageMapping::markAsDeleted);
    }
}