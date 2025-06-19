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
    public void registerReviewImages(String reviewUuid, List<ReviewImageRegisterRequestDto> imageDtos) {

        if (imageDtos == null || imageDtos.isEmpty()) return;

        // 1. imageUrl이 null이거나 빈 문자열인 경우는 필터링
        List<ReviewImageRegisterRequestDto> validDtos = imageDtos.stream()
                .filter(dto -> dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty())
                .toList();

        // 2. 필터링 결과가 비어 있다면 저장하지 않음
        if (validDtos.isEmpty()) return;

        // 3. 유효성 검사
        validateImageConstraints(validDtos);

        // 4. 매핑 정보 생성
        List<ReviewImageMapping> mappingsToSave = new ArrayList<>();
        int imageIndexCounter = 0;

        for (ReviewImageRegisterRequestDto dto : validDtos) {
            MediaType type;
            try {
                type = MediaType.valueOf(dto.getType().toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                continue; // 잘못된 enum 타입은 스킵
            }

            Integer imageIndex = (type == MediaType.IMAGE) ? imageIndexCounter++ : null;

            ReviewImageMapping mapping = ReviewImageMapping.builder()
                    .reviewUuid(reviewUuid)
                    .imageUrl(dto.getImageUrl().trim())
                    .type(type.name())
                    .imageIndex(imageIndex)
                    .build();

            mapping.markAsActive();
            mappingsToSave.add(mapping);
        }

        if (!mappingsToSave.isEmpty()) {
            reviewImageMappingRepository.saveAll(mappingsToSave);
        }
    }

    @Transactional
    @Override
    public void validateImageConstraints(List<ReviewImageRegisterRequestDto> imageDtos) {
        if (imageDtos == null || imageDtos.isEmpty()) return;

        long imageCount = imageDtos.stream()
                .filter(dto -> "IMAGE".equalsIgnoreCase(dto.getType()))
                .count();

        long videoCount = imageDtos.stream()
                .filter(dto -> "VIDEO".equalsIgnoreCase(dto.getType()))
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
    public void markAsDeletedByReviewId(String reviewUuid) {
        List<ReviewImageMapping> images = reviewImageMappingRepository.findAllByReviewUuid(reviewUuid);
        images.forEach(ReviewImageMapping::markAsDeleted);
    }
}