package com.training.store.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException {

    public BadRequestException(ErrorType type) {
        super(type);
    }

    public BadRequestException(ErrorType type, String message) {
        super(type, message);
    }

    public BadRequestException(ErrorType type, HttpStatus status) {
        super(type, status);
    }

    public BadRequestException(ErrorType type, String message, HttpStatus status) {
        super(type, status, message);
    }
}
