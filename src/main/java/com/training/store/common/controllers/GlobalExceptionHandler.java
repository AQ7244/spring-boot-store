package com.training.store.common.controllers;

import com.training.store.common.dtos.ErrorDto;
import com.training.store.common.exceptions.AppException;
import com.training.store.common.exceptions.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
//        var error = new ErrorDto(
//                ErrorType.BAD_REQUEST.getStatus().value(),
//                ErrorType.BAD_REQUEST.getCode(),
//                ErrorType.BAD_REQUEST.name(),
//                ErrorType.BAD_REQUEST.getMessage(),
//                Instant.now(),
//                MDC.get(CorrelationIdFilter.CORRELATION_ID)
//        );
        var error = new ErrorDto(ErrorType.BAD_REQUEST);
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {

        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
//        var error = new ErrorDto(
//                ErrorType.ACCESS_DENIED.getStatus().value(),
//                ErrorType.ACCESS_DENIED.getCode(),
//                ErrorType.ACCESS_DENIED.name(),
//                ex.getMessage(),
//                Instant.now(),
//                MDC.get(CorrelationIdFilter.CORRELATION_ID)
//        );
        var error = new ErrorDto(ErrorType.ACCESS_DENIED, ex.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneric(Exception ex) {
//        var error = new ErrorDto(
//                ErrorType.INTERNAL_SERVER_ERROR.getStatus().value(),
//                ErrorType.INTERNAL_SERVER_ERROR.getCode(),
//                ErrorType.INTERNAL_SERVER_ERROR.name(),
//                ErrorType.INTERNAL_SERVER_ERROR.getMessage(),
//                Instant.now(),
//                MDC.get(CorrelationIdFilter.CORRELATION_ID)
//        );
        var error = new ErrorDto(ErrorType.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> handleAppException(AppException ex) {
//        var error = new ErrorDto(
//                ex.getStatus().value(),
//                ex.getType().getCode(),
//                ex.getType().name(),
//                ex.getMessage(),
//                Instant.now(),
//                MDC.get(CorrelationIdFilter.CORRELATION_ID)
//        );
        var error = new ErrorDto(ex);
        return ResponseEntity.status(ex.getType().getStatus()).body(error);
    }
}
