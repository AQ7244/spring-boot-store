package com.training.store.carts.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
}
