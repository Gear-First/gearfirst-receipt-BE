package com.gearfirst.receipt.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseException {
    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public UnAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
