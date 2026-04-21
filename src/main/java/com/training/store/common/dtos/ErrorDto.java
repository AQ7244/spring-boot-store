package com.training.store.common.dtos;

import com.training.store.common.exceptions.AppException;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.common.filters.CorrelationIdFilter;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorDto(
    int status,
    String errorCode,
    String error,
    String message,
    Instant timestamp,
    String correlationId
) {
    public ErrorDto(ErrorType errorType) {
        this(
                errorType.getStatus().value(),
                errorType.getCode(),
                errorType.name(),
                errorType.getMessage(),
                Instant.now(),
                MDC.get(CorrelationIdFilter.CORRELATION_ID)
        );
    }

    public ErrorDto(ErrorType errorType, String message) {
        this(
                errorType.getStatus().value(),
                errorType.getCode(),
                errorType.name(),
                message,
                Instant.now(),
                MDC.get(CorrelationIdFilter.CORRELATION_ID)
        );
    }

    public ErrorDto(ErrorType errorType, HttpStatus status, String message) {
        this(
                status.value(),
                errorType.getCode(),
                errorType.name(),
                message,
                Instant.now(),
                MDC.get(CorrelationIdFilter.CORRELATION_ID)
        );
    }

    public ErrorDto(AppException exception) {
        this(
                exception.getStatus().value(),
                exception.getType().getCode(),
                exception.getType().name(),
                exception.getMessage(),
                Instant.now(),
                MDC.get(CorrelationIdFilter.CORRELATION_ID)
        );
    }
}
