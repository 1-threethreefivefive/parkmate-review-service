package com.parkmate.reviewservice.review.presentation;

import com.parkmate.reviewservice.common.response.ApiResponse;
import com.parkmate.reviewservice.facade.ReviewFacade;
import com.parkmate.reviewservice.facade.dto.ReviewRegisterRequest;
import com.parkmate.reviewservice.review.application.ReviewService;
import com.parkmate.reviewservice.review.dto.request.ReviewRegisterRequestDto;
import com.parkmate.reviewservice.review.dto.request.ReviewUpdateRequestDto;
import com.parkmate.reviewservice.review.dto.response.ReviewResponseDto;
import com.parkmate.reviewservice.review.vo.request.ReviewRegisterRequestVo;
import com.parkmate.reviewservice.review.vo.request.ReviewUpdateRequestVo;
import com.parkmate.reviewservice.review.vo.response.ReviewResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;
    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 등록",
            description = "리뷰 등록 API 입니다. 리뷰 내용과 평점, 이미지(5장), 동영상(1개)을 등록합니다.",
            tags = {"REVIEW-SERVICE"}
    )
    @PostMapping
    public ApiResponse<Void> registerReview(
            @RequestHeader("X-User-UUID") String userUuid,
            @RequestBody ReviewRegisterRequestVo reviewRegisterRequestVo) {

        ReviewRegisterRequest reviewRegisterRequest = new ReviewRegisterRequest(
                ReviewRegisterRequestDto.of(userUuid, reviewRegisterRequestVo),
                reviewRegisterRequestVo.getImageMappings()
        );

        reviewFacade.registerReview(reviewRegisterRequest);

        return ApiResponse.of(
                HttpStatus.CREATED,
                "리뷰가 등록되었습니다.",
                null
        );
    }

    @Operation(
            summary = "리뷰 조회",
            description = "리뷰 조회 API 입니다. 리뷰 UUID를 PathVariable로 전달하여 리뷰 상세 정보를 조회합니다.",
            tags = {"REVIEW-SERVICE"}
    )
    @GetMapping("/{reviewUuid}")
    public ApiResponse<ReviewResponseVo> getReview(
            @PathVariable String reviewUuid) {

        ReviewResponseDto dto = reviewFacade.getReviewWithImages(reviewUuid);


        ReviewResponseVo responseVo = ReviewResponseVo.from(dto);

        return ApiResponse.of(
                HttpStatus.OK,
                "리뷰 조회에 성공하였습니다.",
                responseVo
        );
    }

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰 수정 API 입니다. 리뷰 UUID와 수정할 내용을 전달하여 리뷰를 수정합니다.",
            tags = {"REVIEW-SERVICE"}
    )
    @PutMapping("/{reviewUuid}")
    public ApiResponse<String> updateReview(
            @RequestHeader("X-User-UUID") String userUuid,
            @PathVariable String reviewUuid,
            @RequestBody ReviewUpdateRequestVo reviewUpdateRequestVo) {

        reviewFacade.updateReview(
                ReviewUpdateRequestDto.of(reviewUuid, userUuid, reviewUpdateRequestVo)
        );

        return ApiResponse.of(
                HttpStatus.OK,
                "리뷰가 수정되었습니다."
        );
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰 삭제 API 입니다. 실제로 삭제하지 않고 삭제 상태로 업데이트 합니다.",
            tags = {"REVIEW-SERVICE"}
    )
    @PutMapping("/{reviewUuid}/softdelete")
    public ApiResponse<Void> softDeleteReview(
            @PathVariable String reviewUuid,
            @RequestHeader("X-User-UUID") String userUuid) {

        reviewFacade.softDeleteReview(reviewUuid, userUuid);

        return ApiResponse.of(
                HttpStatus.OK,
                "리뷰가 삭제(Soft Delete) 처리되었습니다.",
                null
        );
    }
}