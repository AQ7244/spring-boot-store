package com.training.store.payments.services;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.training.store.orders.entities.Order;
import com.training.store.orders.entities.OrderItem;
import com.training.store.orders.enums.PaymentStatus;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.payments.dtos.CheckoutSession;
import com.training.store.payments.dtos.PaymentResult;
import com.training.store.payments.dtos.WebhookRequest;
import com.training.store.payments.exceptions.PaymentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {

        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString());

            order.getItems().forEach(item -> {
                var lineItem = getLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());

        } catch (StripeException exception) {
            System.out.println(exception.getMessage());
            throw new PaymentException(ErrorType.PAYMENT_EXCEPTION);
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {

            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);


            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    return Optional.of(new PaymentResult(this.extractOrderId(event), PaymentStatus.PAID));
                }

                case "payment_intent.payment_failed" -> {
                    return Optional.of(new PaymentResult(this.extractOrderId(event), PaymentStatus.FAILED));
                }
                default -> {
                    return Optional.empty();
                }
            }

        } catch (SignatureVerificationException e) {
            throw new PaymentException(ErrorType.PAYMENT_EXCEPTION, "Invalid Signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(() ->
                new PaymentException(ErrorType.PAYMENT_EXCEPTION, "Could not deserialize Stripe event. Check the SDK and API version."));
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem getLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(getPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData getPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("aed")
                .setUnitAmountDecimal(item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(100)))
                .setProductData(getProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData getProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
