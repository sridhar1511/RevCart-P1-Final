package com.revcart.controller;

import com.revcart.entity.Subscription;
import com.revcart.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = "*")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            if (subscriptionRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already subscribed"));
            }

            Subscription subscription = new Subscription(email);
            subscriptionRepository.save(subscription);
            
            return ResponseEntity.ok(Map.of("message", "Successfully subscribed to newsletter"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error subscribing: " + e.getMessage()));
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            
            Subscription subscription = subscriptionRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));
            
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
            
            return ResponseEntity.ok(Map.of("message", "Successfully unsubscribed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error unsubscribing: " + e.getMessage()));
        }
    }
}
