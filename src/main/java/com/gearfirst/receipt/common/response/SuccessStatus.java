package com.gearfirst.receipt.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {
    /** 200 SUCCESS */
    GET_UNPROCESSED_RECEIPT_SUCCESS(HttpStatus.OK,"미처리 접수 조회 성공"),
    ENGINEER_ASSIGNMENT_SUCCESS(HttpStatus.OK,"엔지니어 배정 성공"),
    REGIST_REPAIR_DETAIL_SUCCESS(HttpStatus.OK,"수리 내역 등록 성공"),
    GET_RECEIPT_DETAIL_SUCCESS(HttpStatus.OK, "접수 내역 조회 성공"),
    GET_MY_RECEIPT_SUCCESS(HttpStatus.OK, "나의 수리 내역 조회 성공"),

    /** 201 CREATED */
    CREATE_SAMPLE_SUCCESS(HttpStatus.CREATED, "샘플 등록 성공"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
