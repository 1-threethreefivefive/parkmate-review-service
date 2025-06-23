package com.parkmate.reviewservice.reviewreactions.presentation;

import com.parkmate.reviewservice.common.response.ApiResponse;
import com.parkmate.reviewservice.reviewreactions.application.ReviewReactionsService;
import com.parkmate.reviewservice.reviewreactions.domain.ReactionType;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionGetRequestDto;
import com.parkmate.reviewservice.reviewreactions.dto.request.ReviewReactionUpsertRequestDto;
import com.parkmate.reviewservice.reviewreactions.vo.request.ReviewReactionUpsertRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewReactionsController {

    private final ReviewReactionsService reviewReactionsService;

    @Operation(
            summary = "리뷰 반응 추가",
            description = "리뷰에 대한 반응을 추가하거나 갱신합니다. ReactionType은 LIKE 또는 DISLIKE 중 하나입니다.",
            tags = {"REVIEW-REACTIONS"}
    )
    @PostMapping("/{reviewUuid}/reactions")
    public ApiResponse<String> addReaction(@PathVariable String reviewUuid,
                                           @RequestHeader("X-User-UUID") String userUuid,
                                           @RequestBody ReviewReactionUpsertRequestVo reviewReactionUpsertRequestVo) {
        reviewReactionsService.addReaction(
                ReviewReactionUpsertRequestDto.of(reviewUuid, userUuid, reviewReactionUpsertRequestVo)
        );
        return ApiResponse.created("리뷰 반응이 추가되었습니다.");
    }

    @Operation(
            summary = "리뷰 반응 조회",
            description = "해당 리뷰에 대해 사용자가 남긴 반응을 조회합니다. 결과는 NONE, LIKE, DISLIKE 중 하나입니다.",
            tags = {"REVIEW-REACTIONS"}
    )
    @GetMapping("/{reviewUuid}/reactions")
    public ApiResponse<ReactionType> getReaction(@PathVariable String reviewUuid,
                                                 @RequestHeader("X-User-UUID") String userUuid) {
        ReactionType type = reviewReactionsService.getReaction(
                ReviewReactionGetRequestDto.of(reviewUuid, userUuid)
        );
        return ApiResponse.of(HttpStatus.OK, "리뷰 반응 조회 성공", type);
    }
}