package com.training.store.orders.mappers;

import com.training.store.orders.dtos.OrderDto;
import com.training.store.orders.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
