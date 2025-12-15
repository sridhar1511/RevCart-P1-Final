package com.revcart.service;

import com.revcart.entity.Coupon;
import com.revcart.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("SAVE10");
        coupon.setDiscountPercentage(10);
        coupon.setActive(true);
        coupon.setMinOrderAmount(new BigDecimal("100.00"));
        coupon.setMaxUses(100);
        coupon.setUsedCount(0);
    }

    @Test
    void testValidateCouponSuccess() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(coupon));

        Optional<Coupon> result = couponService.validateCoupon("SAVE10", new BigDecimal("150.00"));

        assertTrue(result.isPresent());
        assertEquals("SAVE10", result.get().getCode());
        verify(couponRepository, times(1)).findByCode("SAVE10");
    }

    @Test
    void testValidateCouponNotFound() {
        when(couponRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        Optional<Coupon> result = couponService.validateCoupon("INVALID", new BigDecimal("150.00"));

        assertFalse(result.isPresent());
        verify(couponRepository, times(1)).findByCode("INVALID");
    }

    @Test
    void testValidateCouponInactive() {
        coupon.setActive(false);
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(coupon));

        Optional<Coupon> result = couponService.validateCoupon("SAVE10", new BigDecimal("150.00"));

        assertFalse(result.isPresent());
    }

    @Test
    void testValidateCouponMaxUsesExceeded() {
        coupon.setMaxUses(5);
        coupon.setUsedCount(5);
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(coupon));

        Optional<Coupon> result = couponService.validateCoupon("SAVE10", new BigDecimal("150.00"));

        assertFalse(result.isPresent());
    }

    @Test
    void testValidateCouponMinOrderAmountNotMet() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(coupon));

        Optional<Coupon> result = couponService.validateCoupon("SAVE10", new BigDecimal("50.00"));

        assertFalse(result.isPresent());
    }

    @Test
    void testCalculateDiscount() {
        BigDecimal discount = couponService.calculateDiscount(coupon, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("10.00"), discount);
    }

    @Test
    void testUseCoupon() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(coupon));

        couponService.useCoupon("SAVE10");

        verify(couponRepository, times(1)).findByCode("SAVE10");
        verify(couponRepository, times(1)).save(any(Coupon.class));
        assertEquals(1, coupon.getUsedCount());
    }

    @Test
    void testGetAllCoupons() {
        List<Coupon> coupons = new ArrayList<>(List.of(coupon));
        when(couponRepository.findAll()).thenReturn(coupons);

        List<Coupon> result = couponService.getAllCoupons();

        assertEquals(1, result.size());
        verify(couponRepository, times(1)).findAll();
    }

    @Test
    void testCreateCoupon() {
        when(couponRepository.save(coupon)).thenReturn(coupon);

        Coupon result = couponService.createCoupon(coupon);

        assertNotNull(result);
        assertEquals("SAVE10", result.getCode());
        verify(couponRepository, times(1)).save(coupon);
    }
}
