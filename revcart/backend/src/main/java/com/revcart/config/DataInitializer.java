package com.revcart.config;

import com.revcart.entity.Coupon;
import com.revcart.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeCoupons();
    }

    private void initializeCoupons() {
        createCoupon("WELCOME10", 10, new BigDecimal("500.00"), 100);
        createCoupon("MEGA30", 30, new BigDecimal("2000.00"), 25);
        createCoupon("FIRST15", 15, new BigDecimal("300.00"), 200);
        createCoupon("BULK25", 25, new BigDecimal("1500.00"), 30);
        createCoupon("STUDENT5", 5, new BigDecimal("200.00"), 500);
        createCoupon("WEEKEND12", 12, new BigDecimal("800.00"), 75);
        createCoupon("FLASH40", 40, new BigDecimal("3000.00"), 10);
        createCoupon("LOYALTY18", 18, new BigDecimal("600.00"), 100);
        createCoupon("NEWUSER8", 8, new BigDecimal("400.00"), 150);
        createCoupon("SUPER50", 50, new BigDecimal("5000.00"), 5);
    }

    private void createCoupon(String code, int discount, BigDecimal minAmount, int maxUses) {
        if (!couponRepository.findByCode(code).isPresent()) {
            Coupon coupon = new Coupon();
            coupon.setCode(code);
            coupon.setDiscountPercentage(discount);
            coupon.setMinOrderAmount(minAmount);
            coupon.setMaxUses(maxUses);
            coupon.setUsedCount(0);
            coupon.setActive(true);
            couponRepository.save(coupon);
        }
    }
}