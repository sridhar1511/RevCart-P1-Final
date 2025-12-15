package com.revcart.service;

import com.revcart.document.Notification;
import com.revcart.mongo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    public Notification createNotification(Long userId, String title, String message, String type) {
        Notification notification = new Notification(userId, title, message, type);
        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    public void sendOrderConfirmation(com.revcart.entity.User user, com.revcart.entity.Order order) {
        createNotification(user.getId(), "Order Confirmed", "Your order #" + order.getId() + " has been confirmed", "ORDER");
        sendEmailAsync(user.getEmail(), "Order Confirmation", "Your order #" + order.getId() + " has been placed successfully. Total: ₹" + order.getTotalAmount());
    }

    public void sendOrderCancellation(com.revcart.entity.User user, com.revcart.entity.Order order) {
        createNotification(user.getId(), "Order Cancelled", "Your order #" + order.getId() + " has been cancelled", "ORDER");
        sendEmailAsync(user.getEmail(), "Order Cancelled", "Your order #" + order.getId() + " has been cancelled.");
    }

    public void sendPaymentConfirmation(com.revcart.entity.User user, com.revcart.entity.Payment payment) {
        createNotification(user.getId(), "Payment Successful", "Payment of ₹" + payment.getAmount() + " received", "PAYMENT");
        sendEmailAsync(user.getEmail(), "Payment Confirmation", "Your payment of ₹" + payment.getAmount() + " has been received successfully.");
    }

    public void sendRefundNotification(com.revcart.entity.User user, com.revcart.entity.Payment payment) {
        createNotification(user.getId(), "Refund Processed", "Refund of ₹" + payment.getAmount() + " has been initiated", "REFUND");
        sendEmailAsync(user.getEmail(), "Refund Initiated", "Your refund of ₹" + payment.getAmount() + " has been initiated and will be credited within 5-7 business days.");
    }

    public void sendOrderStatusUpdate(com.revcart.entity.User user, com.revcart.entity.Order order) {
        createNotification(user.getId(), "Order Status Updated", "Your order #" + order.getId() + " status is now " + order.getStatus(), "ORDER");
        sendEmailAsync(user.getEmail(), "Order Status Update", "Your order #" + order.getId() + " status has been updated to " + order.getStatus() + ".");
    }

    private void sendEmailAsync(String email, String subject, String message) {
        CompletableFuture.runAsync(() -> {
            try {
                if (email != null && !email.isEmpty()) {
                    emailService.sendOrderConfirmation(email, subject, message);
                }
            } catch (Exception e) {
                System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            }
        });
    }
}
