package com.revcart.controller;

import com.revcart.dto.CartItemRequest;
import com.revcart.entity.Cart;
import com.revcart.entity.CartItem;
import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import com.revcart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            Cart cart = cartService.getCart(user);
            java.util.List<CartItem> items = cart.getCartItems();
            if (items == null) {
                items = new java.util.ArrayList<>();
            }
            java.util.List<Map<String, Object>> cartItems = new java.util.ArrayList<>();
            for (CartItem item : items) {
                if (item.getProduct() != null) {
                    cartItems.add(Map.of(
                        "id", item.getProduct().getId(),
                        "name", item.getProduct().getName(),
                        "price", item.getProduct().getPrice(),
                        "image", item.getProduct().getImage() != null ? item.getProduct().getImage() : "",
                        "quantity", item.getQuantity()
                    ));
                }
            }
            return ResponseEntity.ok(Map.of("cartItems", cartItems));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching cart"));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemRequest request, 
                                      Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            Cart cart = cartService.addToCart(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "Item added to cart", "cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error adding item to cart"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@Valid @RequestBody CartItemRequest request,
                                           Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            Cart cart = cartService.updateCartItem(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "Cart updated", "cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating cart"));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId,
                                           Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            cartService.removeFromCart(user, productId);
            return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error removing item"));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            cartService.clearCart(user);
            Cart clearedCart = cartService.getCart(user);
            return ResponseEntity.ok(Map.of("message", "Cart cleared", "cartItems", clearedCart.getCartItems() != null ? clearedCart.getCartItems() : new java.util.ArrayList<>()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error clearing cart"));
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}