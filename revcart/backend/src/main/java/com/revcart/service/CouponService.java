package com.revcart.service;

import com.revcart.entity.Coupon;
import com.revcart.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public Optional<Coupon> validateCoupon(String code, BigDecimal orderAmount) {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        
        if (coupon.isPresent()) {
            Coupon c = coupon.get();
            if (!c.getActive()) return Optional.empty();
            if (c.getMaxUses() != null && c.getUsedCount() >= c.getMaxUses()) return Optional.empty();
            if (orderAmount.compareTo(c.getMinOrderAmount()) < 0) return Optional.empty();
            return coupon;
        }
        return Optional.empty();
    }

    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
        return orderAmount.multiply(BigDecimal.valueOf(coupon.getDiscountPercentage()))
                .divide(BigDecimal.valueOf(100));
    }

    public void useCoupon(String code) {
        couponRepository.findByCode(code).ifPresent(coupon -> {
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);
        });
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }
}
