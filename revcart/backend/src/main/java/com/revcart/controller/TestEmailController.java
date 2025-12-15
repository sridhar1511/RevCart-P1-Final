package com.revcart.controller;

import com.revcart.entity.Coupon;
import com.revcart.service.CouponService;
import com.revcart.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CouponService couponService;

    @PostMapping("/send-email")
    public ResponseEntity<?> testEmail(@RequestParam String email) {
        try {
            System.out.println("[TestEmailController] Testing email to: " + email);
            System.out.println("[TestEmailController] Email service configured: " + emailService.isEmailConfigured());
            emailService.sendOtpEmail(email, "123456");
            return ResponseEntity.ok(Map.of("message", "Test email sent successfully"));
        } catch (Exception e) {
            System.err.println("[TestEmailController] Failed to send test email: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to send email: " + e.getMessage()));
        }
    }

    @GetMapping("/email-config")
    public ResponseEntity<?> checkEmailConfig() {
        boolean configured = emailService.isEmailConfigured();
        return ResponseEntity.ok(Map.of(
            "configured", configured,
            "message", configured ? "Email service is configured" : "Email service not configured"
        ));
    }

    @PostMapping("/create-coupons")
    public ResponseEntity<?> createTestCoupons() {
        try {
            // Create test coupons manually
            Coupon coupon1 = new Coupon();
            coupon1.setCode("SAVE10");
            coupon1.setDiscountPercentage(10);
            coupon1.setMinOrderAmount(new BigDecimal("100"));
            coupon1.setMaxUses(100);
            coupon1.setUsedCount(0);
            coupon1.setActive(true);
            couponService.createCoupon(coupon1);

            Coupon coupon2 = new Coupon();
            coupon2.setCode("WELCOME20");
            coupon2.setDiscountPercentage(20);
            coupon2.setMinOrderAmount(new BigDecimal("200"));
            coupon2.setMaxUses(50);
            coupon2.setUsedCount(0);
            coupon2.setActive(true);
            couponService.createCoupon(coupon2);

            Coupon coupon3 = new Coupon();
            coupon3.setCode("BIGDEAL");
            coupon3.setDiscountPercentage(15);
            coupon3.setMinOrderAmount(new BigDecimal("500"));
            coupon3.setMaxUses(25);
            coupon3.setUsedCount(0);
            coupon3.setActive(true);
            couponService.createCoupon(coupon3);

            return ResponseEntity.ok(Map.of("message", "Test coupons created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating coupons: " + e.getMessage()));
        }
    }
}