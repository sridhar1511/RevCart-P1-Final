package com.revcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean isEmailConfigured() {
        boolean configured = mailSender != null && fromEmail != null && !fromEmail.isEmpty();
        System.out.println("[EmailService] Email configured: " + configured);
        System.out.println("[EmailService] MailSender: " + (mailSender != null ? "Available" : "NULL"));
        System.out.println("[EmailService] From email: " + fromEmail);
        return configured;
    }

    public void sendOtpEmail(String email, String otp) {
        sendEmail(email, "RevCart - Email Verification OTP", "Your OTP for email verification is: " + otp + "\nThis OTP is valid for 10 minutes.");
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        sendEmail(email, "RevCart - Password Reset", "Click the link below to reset your password:\n" + resetLink + "\nThis link is valid for 1 hour.");
    }

    public void sendOrderConfirmation(String email, String subject, String message) {
        sendEmail(email, subject, message);
    }

    public void sendDeliveryNotification(String email, String orderStatus, String estimatedTime) {
        sendEmail(email, "RevCart - Delivery Update", "Your order status: " + orderStatus + "\nEstimated delivery time: " + estimatedTime);
    }

    private void sendEmail(String email, String subject, String message) {
        System.out.println("[EmailService] Attempting to send email to: " + email);
        System.out.println("[EmailService] From email: " + fromEmail);
        System.out.println("[EmailService] MailSender is null: " + (mailSender == null));
        
        if (mailSender == null) {
            System.out.println("[EmailService] ERROR: Email service not configured");
            return;
        }
        
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom(fromEmail);
            System.out.println("[EmailService] Sending email...");
            mailSender.send(mailMessage);
            System.out.println("[EmailService] Email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("[EmailService] ERROR: Failed to send email to " + email);
            System.err.println("[EmailService] Exception: " + e.getClass().getName());
            System.err.println("[EmailService] Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
