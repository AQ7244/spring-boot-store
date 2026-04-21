package com.training.store.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends AppException {

    public ConflictException(ErrorType type) {
        super(type);
    }

    public ConflictException(ErrorType type, String message) {
        super(type, message);
    }

    public ConflictException(ErrorType type, HttpStatus status) {
        super(type, status);
    }

    public ConflictException(ErrorType type, String message, HttpStatus status) {
        super(type, status, message);
    }
}
