package com.training.store.payments.controllers;

import com.training.store.payments.dtos.CheckoutRequest;
import com.training.store.payments.dtos.CheckoutResponse;
import com.training.store.payments.services.CheckoutService;
import com.training.store.payments.dtos.WebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
@Tag(name = "Checkout", description = "Operations related to checkout process")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    @Operation(
            summary = "Checkout",
            description = "Creates an order from the user's cart, processes payment, and completes the checkout process."
    )
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    @Operation(
            summary = "Handle payment webhook",
            description = "Receives webhook events from the payment provider and updates the payment status of an order."
    )
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    //region Exception Handling
    /*
    * Below two exception are merged into ResourceNotFoundException and that single exception is being used for these multiple purposes
    *
    @ExceptionHandler({CartNotFoundException.class, EmptyCartException.class})
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        var error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }
     */
    //endregion
}
