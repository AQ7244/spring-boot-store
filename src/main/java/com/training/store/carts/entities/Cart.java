package com.training.store.carts.entities;

import com.training.store.products.entities.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)   // get totalPrice of each item
                .reduce(BigDecimal.ZERO, BigDecimal::add); // sum them
    }

    public CartItem getItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product, Integer quantity) {
        var cartItem = this.getItem(product.getId());
        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(this)
                    .build();
            this.items.add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        return cartItem;
    }

    public void removeItem(Long productId) {
        var cartItem = this.getItem(productId);
        if (cartItem == null) {
            return;
        }

        this.items.remove(cartItem);
        cartItem.setCart(null);
    }

    public void clearCart(UUID cartId) {
//        this.items.forEach(item -> item.setCart(null));
        this.items.clear();
        return;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }
}
