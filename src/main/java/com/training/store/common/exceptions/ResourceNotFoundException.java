package com.training.store.common.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(ErrorType type) {
        super(type);
    }

    public ResourceNotFoundException(ErrorType type, String message) {
        super(type, message);
    }

    public ResourceNotFoundException(ErrorType type, HttpStatus status) {
        super(type, status);
    }

    public ResourceNotFoundException(ErrorType type, String message, HttpStatus status) {
        super(type, status, message);
    }
}
