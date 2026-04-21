package com.training.store.carts.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided.")
    @Min(value = 1, message = "Please enter quantity between 1 and 100.")
    @Max(value = 100, message = "Please enter quantity between 1 and 100.")
    private Integer quantity;
}
