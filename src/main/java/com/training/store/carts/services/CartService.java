package com.training.store.carts.services;

import com.training.store.carts.dtos.CartDto;
import com.training.store.carts.dtos.CartItemDto;
import com.training.store.carts.entities.Cart;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.common.exceptions.ResourceNotFoundException;
import com.training.store.carts.mappers.CartMapper;
import com.training.store.carts.repositories.CartRepository;
import com.training.store.products.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId, Integer quantity) {
        var cart = this.getCartInstance(cartId);

        var product = productRepository.findProductWithCategory(productId).orElse(null);
        if (product == null) {
            throw new ResourceNotFoundException(ErrorType.PRODUCT_NOT_FOUND);
        }

        var cartItem = cart.addItem(product, quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = this.getCartInstance(cartId);
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        var cart = this.getCartInstance(cartId);

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    Map.of("error", "Product not found in the Cart.")
//            );
            throw new ResourceNotFoundException(ErrorType.PRODUCT_NOT_FOUND);
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = this.getCartInstance(cartId);

        cart.removeItem(productId);
        cartRepository.save(cart);
        return;
    }

    public void clearCart(UUID cartId) {

        var cart = this.getCartInstance(cartId);
        cart.clearCart(cartId);
        cartRepository.save(cart);
        return;
    }

    // ========================
    // Private Helpers
    // ========================
    //region Helper Methods

    private Cart getCartInstance(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new ResourceNotFoundException(ErrorType.CART_NOT_FOUND);
        }

        return cart;
    }

    //endregion
}
