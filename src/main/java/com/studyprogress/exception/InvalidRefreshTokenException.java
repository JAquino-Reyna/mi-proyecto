package com.studyprogress.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApiException {
    public InvalidRefreshTokenException(String message) { super(HttpStatus.UNAUTHORIZED, message); }
}
