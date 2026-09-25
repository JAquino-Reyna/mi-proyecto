package com.studyprogress.exception;

import org.springframework.http.HttpStatus;

public class InvalidReminderException extends ApiException {
    public InvalidReminderException(String message) { super(HttpStatus.BAD_REQUEST, message); }
}
