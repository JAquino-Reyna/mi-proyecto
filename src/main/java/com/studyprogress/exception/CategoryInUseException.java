package com.studyprogress.exception;

import org.springframework.http.HttpStatus;

public class CategoryInUseException extends ApiException {
    public CategoryInUseException(String message) { super(HttpStatus.CONFLICT, message); }
}
