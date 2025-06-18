package com.parkmate.reviewservice.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    // ✅ 2xx: 성공
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),
    REVIEW_REGISTERED(HttpStatus.CREATED, true, 201, "리뷰가 등록되었습니다."),
    REVIEW_UPDATED(HttpStatus.OK, true, 200, "리뷰가 수정되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, true, 200, "리뷰가 삭제되었습니다."),

    // ❌ 4xx: 클라이언트 오류
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, false, 400, "잘못된 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "리소스가 존재하지 않습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "리뷰를 찾을 수 없습니다."),
    REVIEW_FORBIDDEN(HttpStatus.FORBIDDEN, false, 403, "리뷰에 대한 권한이 없습니다."),
    REVIEW_ALREADY_EXISTS_FOR_PARKING_LOT(HttpStatus.CONFLICT, false, 409, "이미 해당 주차장에 대해 리뷰가 등록되었습니다."),
    REVIEW_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, false, 400, "이미지는 최대 5장까지 등록할 수 있습니다."),
    REVIEW_VIDEO_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, false, 400, "동영상은 최대 1개까지만 등록할 수 있습니다."),
    REVIEW_ALREADY_DELETED(HttpStatus.BAD_REQUEST, false, 400, "이미 삭제된 리뷰입니다."),

    // ❗ 5xx: 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 500, "서버 내부 오류가 발생했습니다."),
    REVIEW_IMAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 500, "리뷰 이미지 저장에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}