package com.training.store.carts.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemsToCartRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;
    private Integer quantity = 1;
}
