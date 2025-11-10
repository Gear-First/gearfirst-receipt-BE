package com.gearfirst.receipt.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorStatus {
    /** 400 BAD_REQUEST */
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 값이 입력되지 않았습니다."),
    INVALID_USER_EXCEPTION(HttpStatus.BAD_REQUEST,"잘못된 사용자 정보 형식입니다. (Base64 디코딩 실패)"),

    /** 401 UNAUTHORIZED */
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    /** 404 NOT_FOUND */
    NOT_FOUND_MEMBER_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다."),

    /** 500 SERVER_ERROR */
    FAIL_UPLOAD_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드 실패하였습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
