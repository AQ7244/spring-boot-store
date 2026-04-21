package com.training.store.orders.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private OrderProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
