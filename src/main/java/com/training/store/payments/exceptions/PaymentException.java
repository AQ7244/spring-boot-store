package com.training.store.payments.exceptions;

import com.training.store.common.exceptions.AppException;
import com.training.store.common.exceptions.ErrorType;
import org.springframework.http.HttpStatus;

public class PaymentException extends AppException {

    public PaymentException(ErrorType type) {
        super(type);
    }

    public PaymentException(ErrorType type, String message) {
        super(type, message);
    }

    public PaymentException(ErrorType type, HttpStatus status) {
        super(type, status);
    }

    public PaymentException(ErrorType type, String message, HttpStatus status) {
        super(type, status, message);
    }
}