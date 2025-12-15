package com.revcart.service;

import com.revcart.entity.*;
import com.revcart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getOrCreateCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        Cart newCart = new Cart(user);
        return cartRepository.save(newCart);
    }

    public Cart addToCart(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem);
        }

        return cartRepository.findById(cart.getId()).orElse(cart);
    }

    public Cart updateCartItem(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }

        return cartRepository.findById(cart.getId()).orElse(cart);
    }

    public void removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        existingItem.ifPresent(cartItemRepository::delete);
    }

    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        if (cart.getCartItems() != null) {
            cartItemRepository.deleteAll(cart.getCartItems());
        }
        cartItemRepository.deleteByCart(cart);
    }

    public Cart getCart(User user) {
        Cart cart = getOrCreateCart(user);
        if (cart.getCartItems() != null) {
            cart.getCartItems().size();
        }
        return cart;
    }

    public Cart getCartByUser(User user) {
        Cart cart = getOrCreateCart(user);
        if (cart.getCartItems() != null) {
            cart.getCartItems().size();
        }
        return cart;
    }
}