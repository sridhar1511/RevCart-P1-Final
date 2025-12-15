package com.revcart.controller;

import com.revcart.service.PaymentService;
import com.revcart.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
public class RazorpayController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam Long orderId,
                                        @RequestParam Double amount,
                                        @RequestParam(defaultValue = "INR") String currency) {
        try {
            Map<String, Object> order = razorpayService.createOrder(orderId, amount, currency);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "order", order,
                "keyId", razorpayService.getKeyId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestParam String razorpayOrderId,
                                          @RequestParam String razorpayPaymentId,
                                          @RequestParam String razorpaySignature) {
        try {
            Map<String, Object> verification = razorpayService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            
            if ((Boolean) verification.get("valid")) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Payment verified successfully", "verification", verification));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Payment verification failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/capture-payment")
    public ResponseEntity<?> capturePayment(@RequestParam String razorpayPaymentId,
                                           @RequestParam Double amount) {
        try {
            Map<String, Object> captured = razorpayService.capturePayment(razorpayPaymentId, amount);
            return ResponseEntity.ok(Map.of("success", true, "payment", captured));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/refund-payment")
    public ResponseEntity<?> refundPayment(@RequestParam String razorpayPaymentId,
                                          @RequestParam Double amount) {
        try {
            Map<String, Object> refund = razorpayService.refundPayment(razorpayPaymentId, amount);
            return ResponseEntity.ok(Map.of("success", true, "refund", refund));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
