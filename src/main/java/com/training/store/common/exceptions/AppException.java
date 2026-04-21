package com.training.store.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {

    private final ErrorType type;
    private final HttpStatus status;
    private final String message;

    protected AppException(ErrorType type) {
        super(type.getMessage());
        this.type = type;
        this.status = null;
        this.message = null;
    }

    protected AppException(ErrorType type, String message) {
        super(message);
        this.type = type;
        this.message = message;
        this.status = null;
    }

    protected AppException(ErrorType type, HttpStatus status) {
        super(type.getMessage());
        this.type = type;
        this.status = status;
        this.message = null;
    }

    protected AppException(ErrorType type, HttpStatus status, String message) {
        super(message);
        this.type = type;
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status != null ? status : type.getStatus();
    }

    public String getMessage() {
        return message != null ? message : type.getMessage();
    }
}
