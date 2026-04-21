package com.training.store.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // AUTH_1001 - AUTH_1999
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "1002", "User already exists"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "1003", "Access denied."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "1004", "You are not authenticated to access this resource."),
    // ORDER_2001 - ORDER_2999
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"2001", "Order not found"),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "2002", "You cannot access this order"),
    // PRODUCT_3001 - PRODUCT_3999
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "Product not found in the cart."),
    // CART_4001 - CART_4999
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "4001", "Cart not found."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "4002", "Cart is empty"),
    // PAYMENT_5001 - PAYMENT_5999
    PAYMENT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "5001", "Error creating a checkout session"),
    // BAD_9998
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "9998", "Invalid Request body."),
    // INTERNAL_9999
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "Something went wrong.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorType(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}