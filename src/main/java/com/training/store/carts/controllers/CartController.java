package com.training.store.carts.controllers;

import com.training.store.carts.dtos.AddItemsToCartRequest;
import com.training.store.carts.dtos.CartDto;
import com.training.store.carts.dtos.CartItemDto;
import com.training.store.carts.dtos.UpdateCartItemRequest;
import com.training.store.carts.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder
    ) {

        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Adds a product to the cart.")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart.")
            @PathVariable(name = "id") UUID cartId,
            @Valid @RequestBody AddItemsToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {

        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ) {

        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {

        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    //region Exception Handling
    /*
    * Below two exception are merged into ResourceNotFoundException and that single exception is being used for these multiple purposes
    *

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> cartNotFoundExceptionHandler() {

        var error = new ErrorDto(
                HttpStatus.NOT_FOUND.value(),
                "Cart not found.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> productNotFoundExceptionHandler() {
        var error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                "Product not found in the cart.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }
    */
    //endregion
}
