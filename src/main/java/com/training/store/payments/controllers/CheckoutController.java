package com.training.store.payments.controllers;

import com.training.store.payments.dtos.CheckoutRequest;
import com.training.store.payments.dtos.CheckoutResponse;
import com.training.store.payments.services.CheckoutService;
import com.training.store.payments.dtos.WebhookRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
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
