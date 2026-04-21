package com.training.store.products.dtos;

import com.training.store.common.validation.constraint.CategoryExists;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required.")
    @Size(min = 3, max = 150, message = "Please enter a product name between 3 and 150 characters.")
    private String name;

    @NotEmpty(message = "Product description is required.")
    @Size(min = 1, max = 250, message = "Please enter a product description between 1 and 250 characters.")
    private String description;

    @NotNull(message = "Product price is required.")
    @Positive(message = "Price must be greater than zero")
    @DecimalMin(value = "1.00", message = "Minimum price is 1.00")
    @DecimalMax(value = "100000.00", message = "Price exceeds allowed limit of 100000.00")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 6 digits before the decimal and up to 2 digits after (e.g., 999999.99)")
    private BigDecimal price;

    @NotNull(message = "Category ID is required to link the product.")
    @Positive(message = "Category ID must be a positive number.")
    @CategoryExists
    private Byte categoryId;
}
