package com.revcart.service;

import com.revcart.entity.Order;
import com.revcart.entity.Payment;
import com.revcart.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private NotificationService notificationService;

    public Payment createPayment(Order order, Payment.PaymentMethod method) {
        if (order == null || order.getTotalAmount() == null) {
            throw new RuntimeException("Invalid order or amount");
        }
        Payment payment = new Payment(order, order.getTotalAmount(), method);
        return paymentRepository.save(payment);
    }

    public Payment createPaymentForOrder(Long orderId, Payment.PaymentMethod method) {
        if (orderId == null || method == null) {
            throw new RuntimeException("Order ID and payment method are required");
        }
        Order order = orderService.getOrderById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        if (order.getTotalAmount() == null) {
            throw new RuntimeException("Order total amount is not set");
        }
        return createPayment(order, method);
    }

    public Payment processPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).map(payment -> {
            try {
                payment.setTransactionId(UUID.randomUUID().toString());
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                payment.setGatewayResponse("Payment processed successfully");
                Payment savedPayment = paymentRepository.save(payment);
                
                try {
                    orderService.updateOrderStatus(payment.getOrder().getId(), Order.OrderStatus.CONFIRMED);
                    notificationService.sendPaymentConfirmation(payment.getOrder().getUser(), payment);
                } catch (Exception notifError) {
                    System.err.println("Notification error: " + notifError.getMessage());
                }
                
                return savedPayment;
            } catch (Exception e) {
                throw new RuntimeException("Payment processing failed: " + e.getMessage());
            }
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment failPayment(Long paymentId, String reason) {
        return paymentRepository.findById(paymentId).map(payment -> {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setGatewayResponse(reason);
            paymentRepository.save(payment);
            
            orderService.cancelOrder(payment.getOrder().getId());
            return payment;
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Optional<Payment> getPaymentByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }

    public Payment refundPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).map(payment -> {
            if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
                paymentRepository.save(payment);
                notificationService.sendRefundNotification(payment.getOrder().getUser(), payment);
            }
            return payment;
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
