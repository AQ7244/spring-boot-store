package com.training.store.payments.services;

import com.training.store.orders.entities.Order;
import com.training.store.payments.dtos.CheckoutSession;
import com.training.store.payments.dtos.PaymentResult;
import com.training.store.payments.dtos.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
