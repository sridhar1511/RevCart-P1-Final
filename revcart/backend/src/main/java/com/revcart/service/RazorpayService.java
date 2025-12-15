package com.revcart.service;

import com.revcart.entity.Payment;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RazorpayService {

    private static final String RAZORPAY_KEY_ID = "rzp_test_1234567890";
    private static final String RAZORPAY_KEY_SECRET = "test_secret_key";

    public Map<String, Object> createOrder(Long orderId, Double amount, String currency) {
        Map<String, Object> response = new HashMap<>();
        String razorpayOrderId = "order_" + UUID.randomUUID().toString().substring(0, 12);
        
        response.put("id", razorpayOrderId);
        response.put("entity", "order");
        response.put("amount", (int)(amount * 100)); // Amount in paise
        response.put("amount_paid", 0);
        response.put("amount_due", (int)(amount * 100));
        response.put("currency", currency);
        response.put("receipt", "order_" + orderId);
        response.put("status", "created");
        response.put("attempts", 0);
        response.put("notes", Map.of("orderId", orderId));
        response.put("created_at", System.currentTimeMillis() / 1000);
        
        return response;
    }

    public Map<String, Object> verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        Map<String, Object> response = new HashMap<>();
        
        // Mock verification - in production, verify signature using HMAC-SHA256
        boolean isValid = verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
        
        response.put("valid", isValid);
        response.put("paymentId", razorpayPaymentId);
        response.put("orderId", razorpayOrderId);
        response.put("status", isValid ? "captured" : "failed");
        
        return response;
    }

    public Map<String, Object> capturePayment(String razorpayPaymentId, Double amount) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("id", razorpayPaymentId);
        response.put("entity", "payment");
        response.put("amount", (int)(amount * 100));
        response.put("currency", "INR");
        response.put("status", "captured");
        response.put("method", "card");
        response.put("description", "RevCart Order Payment");
        response.put("captured", true);
        response.put("email", "customer@example.com");
        response.put("contact", "+919999999999");
        response.put("fee", (int)(amount * 100 * 0.02)); // 2% fee
        response.put("tax", 0);
        response.put("created_at", System.currentTimeMillis() / 1000);
        
        return response;
    }

    public Map<String, Object> refundPayment(String razorpayPaymentId, Double amount) {
        Map<String, Object> response = new HashMap<>();
        String refundId = "rfnd_" + UUID.randomUUID().toString().substring(0, 12);
        
        response.put("id", refundId);
        response.put("entity", "refund");
        response.put("payment_id", razorpayPaymentId);
        response.put("amount", (int)(amount * 100));
        response.put("currency", "INR");
        response.put("status", "processed");
        response.put("speed_processed", "normal");
        response.put("created_at", System.currentTimeMillis() / 1000);
        
        return response;
    }

    private boolean verifySignature(String orderId, String paymentId, String signature) {
        // Mock verification - always return true for testing
        // In production: use HMAC-SHA256 to verify signature
        return signature != null && !signature.isEmpty();
    }

    public String getKeyId() {
        return RAZORPAY_KEY_ID;
    }
}
