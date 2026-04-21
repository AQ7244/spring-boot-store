package com.training.store.carts.mappers;

import com.training.store.carts.dtos.CartDto;
import com.training.store.carts.dtos.CartItemDto;
import com.training.store.carts.entities.Cart;
import com.training.store.carts.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice", expression = "java(item.getTotalPrice())")
    CartItemDto toDto(CartItem item);
}
