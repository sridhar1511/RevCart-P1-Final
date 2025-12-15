package com.revcart.controller;

import com.revcart.entity.Wishlist;
import com.revcart.entity.Product;
import com.revcart.repository.WishlistRepository;
import com.revcart.repository.ProductRepository;
import com.revcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getWishlist(Authentication auth) {
        try {
            if (auth == null) {
                return ResponseEntity.ok(Map.of("products", new java.util.ArrayList<>()));
            }
            String email = auth.getName();
            var user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(Map.of("products", new java.util.ArrayList<>()));
            }
            var wishlist = wishlistRepository.findByUserId(user.getId()).orElse(null);
            if (wishlist == null) {
                return ResponseEntity.ok(Map.of("products", new java.util.ArrayList<>()));
            }
            return ResponseEntity.ok(Map.of("products", wishlist.getProducts()));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("products", new java.util.ArrayList<>()));
        }
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long productId, Authentication auth) {
        try {
            if (auth == null) {
                return ResponseEntity.ok(Map.of("message", "Please login to add to wishlist"));
            }
            String email = auth.getName();
            var user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(Map.of("message", "User not found"));
            }
            var product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                return ResponseEntity.ok(Map.of("message", "Product not found"));
            }
            
            var wishlist = wishlistRepository.findByUserId(user.getId()).orElse(null);
            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlist = wishlistRepository.save(wishlist);
            }
            
            if (!wishlist.getProducts().contains(product)) {
                wishlist.getProducts().add(product);
                wishlistRepository.save(wishlist);
            }
            
            return ResponseEntity.ok(Map.of("message", "Added to wishlist"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId, Authentication auth) {
        try {
            String email = auth.getName();
            var user = userRepository.findByEmail(email).orElseThrow();
            var wishlist = wishlistRepository.findByUserId(user.getId()).orElseThrow();
            var product = productRepository.findById(productId).orElseThrow();
            
            wishlist.getProducts().remove(product);
            wishlistRepository.save(wishlist);
            
            return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error removing from wishlist"));
        }
    }
}
