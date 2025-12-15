package com.revcart.controller;

import com.revcart.entity.Order;
import com.revcart.entity.Payment;
import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import com.revcart.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestParam Long orderId,
                                          @RequestParam String method,
                                          Authentication authentication) {
        try {
            if (orderId == null || method == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Order ID and payment method are required"));
            }
            
            String methodUpper = method.toUpperCase().trim();
            Payment.PaymentMethod paymentMethod;
            
            try {
                paymentMethod = Payment.PaymentMethod.valueOf(methodUpper);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid payment method: " + method + ". Valid methods: COD, CARD, UPI, NET_BANKING"));
            }
            
            Payment payment = paymentService.createPaymentForOrder(orderId, paymentMethod);
            return ResponseEntity.ok(Map.of("message", "Payment created", "payment", payment));
        } catch (Exception e) {
            System.err.println("Payment error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("message", "Payment creation failed: " + e.getMessage()));
        }
    }

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<?> processPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.processPayment(paymentId);
            return ResponseEntity.ok(Map.of("message", "Payment processed successfully", "payment", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<?> failPayment(@PathVariable Long paymentId,
                                        @RequestParam String reason) {
        try {
            Payment payment = paymentService.failPayment(paymentId, reason);
            return ResponseEntity.ok(Map.of("message", "Payment marked as failed", "payment", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(Map.of("message", "Refund processed", "payment", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
