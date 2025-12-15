package com.revcart.controller;

import com.revcart.entity.Product;
import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import com.revcart.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/for-user")
    public ResponseEntity<?> getRecommendationsForUser(Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Product> recommendations = recommendationService.getRecommendationsForUser(user);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching recommendations"));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularProducts() {
        try {
            List<Product> products = recommendationService.getPopularProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching popular products"));
        }
    }

    @GetMapping("/similar/{productId}")
    public ResponseEntity<?> getSimilarProducts(@PathVariable Long productId) {
        try {
            List<Product> products = recommendationService.getSimilarProducts(productId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching similar products"));
        }
    }
}
