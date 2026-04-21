package com.training.store.orders.services;

import com.training.store.auth.services.AuthService;
import com.training.store.orders.dtos.OrderDto;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.common.exceptions.ResourceNotFoundException;
import com.training.store.orders.mappers.OrderMapper;
import com.training.store.orders.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {

        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorType.ORDER_NOT_FOUND));

        var currentUser = authService.getCurrentUser();
        if (!order.isPlacedBy(currentUser)) {
            throw new AccessDeniedException("You don't have access to this order.");
        }

        return orderMapper.toDto(order);
    }
}
