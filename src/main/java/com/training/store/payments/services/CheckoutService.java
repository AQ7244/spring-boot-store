package com.training.store.payments.services;

import com.training.store.payments.dtos.CheckoutRequest;
import com.training.store.payments.dtos.CheckoutResponse;
import com.training.store.orders.entities.Order;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.payments.dtos.WebhookRequest;
import com.training.store.payments.exceptions.PaymentException;
import com.training.store.common.exceptions.ResourceNotFoundException;
import com.training.store.carts.repositories.CartRepository;
import com.training.store.orders.repositories.OrderRepository;
import com.training.store.auth.services.AuthService;
import com.training.store.carts.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {

        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new ResourceNotFoundException(ErrorType.CART_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        if (cart.isEmpty()) {
            throw new ResourceNotFoundException(ErrorType.CART_EMPTY);
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(
                    order.getId(),
                    session.getCheckoutUrl()
            );

        } catch (PaymentException exception) {
            orderRepository.delete(order);
            throw exception;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
            .parseWebhookRequest(request)
            .ifPresent(paymentResult -> {
                var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                order.setStatus(paymentResult.getPaymentStatus());
                orderRepository.save(order);
            });

    }
}
