package com.letter.cookies.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum CustomResponseStatus {

    // 1000번대 성공 응답 코드
    SUCCESS(true, 1000, "요청에 성공하였습니다", HttpStatus.OK),

    // 4000번대 오류 응답 코드
    REQUEST_DATA_NULL(false, 4000, "필수 항목이 입력되지 않았습니다", HttpStatus.BAD_REQUEST),
    REQUEST_DATA_DOES_NOT_EXISTS(false, 4002, "존재하지 않는 편지 입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_USER_NOT_EXISTS(false, 4003, "사용자가 존재하지 않습니다.", HttpStatus.FORBIDDEN),
    REQUEST_NOT_ENOUGH_COOKIE(false, 4007, "쿠키가 충분하지 않습니다." , HttpStatus.BAD_REQUEST);

    private final boolean isSuccess;
    private final int responseCode;
    private final String message;

    private final HttpStatus httpStatus;

    CustomResponseStatus(boolean isSuccess, int responseCode, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.responseCode = responseCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}