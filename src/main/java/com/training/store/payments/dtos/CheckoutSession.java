package com.training.store.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckoutSession {
    private String checkoutUrl;
}
