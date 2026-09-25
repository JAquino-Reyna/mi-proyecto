package com.studyprogress.exception;

import org.springframework.http.HttpStatus;

public class EmailDeliveryException extends ApiException {
    public EmailDeliveryException(String message) { super(HttpStatus.BAD_GATEWAY, message); }
}
