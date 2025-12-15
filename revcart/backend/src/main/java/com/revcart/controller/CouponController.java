package com.revcart.controller;

import com.revcart.entity.Coupon;
import com.revcart.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:4200")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateCoupon(@RequestBody Map<String, Object> request) {
        String code = (String) request.get("code");
        BigDecimal orderAmount = new BigDecimal(request.get("orderAmount").toString());
        
        var coupon = couponService.validateCoupon(code, orderAmount);
        if (coupon.isPresent()) {
            Coupon c = coupon.get();
            BigDecimal discount = couponService.calculateDiscount(c, orderAmount);
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "discount", discount,
                "discountPercentage", c.getDiscountPercentage(),
                "finalAmount", orderAmount.subtract(discount)
            ));
        }
        return ResponseEntity.ok(Map.of("valid", false, "message", "Invalid or expired coupon"));
    }

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }
}
