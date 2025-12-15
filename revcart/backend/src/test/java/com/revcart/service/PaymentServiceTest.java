package com.revcart.service;

import com.revcart.entity.Order;
import com.revcart.entity.Payment;
import com.revcart.entity.User;
import com.revcart.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123");
        user.setId(1L);

        order = new Order(user, new BigDecimal("100.00"), "123 Main St", "9876543210");
        order.setId(1L);

        payment = new Payment(order, new BigDecimal("100.00"), Payment.PaymentMethod.CARD);
        payment.setId(1L);
    }

    @Test
    void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment(order, Payment.PaymentMethod.CARD);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePaymentNullOrder() {
        assertThrows(RuntimeException.class, () -> paymentService.createPayment(null, Payment.PaymentMethod.CARD));
    }

    @Test
    void testCreatePaymentForOrder() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPaymentForOrder(1L, Payment.PaymentMethod.CARD);

        assertNotNull(result);
        verify(orderService, times(1)).getOrderById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePaymentForOrderNotFound() {
        when(orderService.getOrderById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.createPaymentForOrder(999L, Payment.PaymentMethod.CARD));
    }

    @Test
    void testProcessPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.processPayment(1L);

        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.SUCCESS, result.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testProcessPaymentNotFound() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.processPayment(999L));
    }

    @Test
    void testFailPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.failPayment(1L, "Insufficient funds");

        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.FAILED, result.getStatus());
        assertEquals("Insufficient funds", result.getGatewayResponse());
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderService, times(1)).cancelOrder(1L);
    }

    @Test
    void testGetPaymentByOrder() {
        when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentService.getPaymentByOrder(order);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(paymentRepository, times(1)).findByOrder(order);
    }

    @Test
    void testRefundPayment() {
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.refundPayment(1L);

        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.REFUNDED, result.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
        verify(notificationService, times(1)).sendRefundNotification(user, payment);
    }
}
