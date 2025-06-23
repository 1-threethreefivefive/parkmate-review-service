package com.parkmate.reviewservice.reviewimagemapping.application;

import com.parkmate.reviewservice.common.exception.BaseException;
import com.parkmate.reviewservice.common.response.ResponseStatus;
import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMapping;
import com.parkmate.reviewservice.reviewimagemapping.domain.ReviewImageMappingStatus;
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
    public void registerReviewImages(String reviewUuid, List<ReviewImageRegisterRequestDto> imageDtos) {

        if (imageDtos == null || imageDtos.isEmpty()) return;

        validateImageConstraints(imageDtos);

        List<ReviewImageRegisterRequestDto> validDtos = imageDtos.stream()
                .filter(dto -> dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty())
                .toList();

        markAsDeletedByReviewUuid(reviewUuid);

        List<ReviewImageMapping> mappingsToSave = new ArrayList<>();
        int imageIndexCounter = 0;

        for (ReviewImageRegisterRequestDto dto : validDtos) {
            Integer imageIndex = MediaType.IMAGE.name().equals(dto.getType()) ? imageIndexCounter++ : null;

            ReviewImageMapping mapping = ReviewImageMapping.builder()
                    .reviewUuid(reviewUuid)
                    .imageUrl(dto.getImageUrl())
                    .type(dto.getType())
                    .imageIndex(imageIndex)
                    .build();

            mapping.markAsActive();
            mappingsToSave.add(mapping);
        }

        if (!mappingsToSave.isEmpty()) {
            reviewImageMappingRepository.saveAll(mappingsToSave);
        }
    }

    private void validateImageConstraints(List<ReviewImageRegisterRequestDto> imageMappings) {
        long imageCount = imageMappings.stream()
                .filter(dto -> MediaType.IMAGE.name().equals(dto.getType()))
                .count();

        long videoCount = imageMappings.stream()
                .filter(dto -> MediaType.VIDEO.name().equals(dto.getType()))
                .count();

        if (imageCount > 5) {
            throw new BaseException(ResponseStatus.REVIEW_IMAGE_LIMIT_EXCEEDED);
        }

        if (videoCount > 1) {
            throw new BaseException(ResponseStatus.REVIEW_VIDEO_LIMIT_EXCEEDED);
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
        images.forEach(ReviewImageMapping::markAsDeleted);
    }
}