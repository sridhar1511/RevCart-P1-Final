package com.revcart.service;

import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123");
        user.setId(1L);
        ReflectionTestUtils.setField(authenticationService, "frontendUrl", "http://localhost:4200");
    }

    @Test
    void testSendOtp() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        authenticationService.sendOtp("john@example.com");

        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendOtpEmail(eq("john@example.com"), anyString());
    }

    @Test
    void testSendOtpUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.sendOtp("notfound@example.com"));
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void testVerifyOtp() {
        user.setOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        authenticationService.verifyOtp("john@example.com", "123456");

        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        assertTrue(user.getIsVerified());
    }

    @Test
    void testVerifyOtpInvalid() {
        user.setOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authenticationService.verifyOtp("john@example.com", "654321"));
    }

    @Test
    void testVerifyOtpExpired() {
        user.setOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().minusMinutes(10));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authenticationService.verifyOtp("john@example.com", "123456"));
    }

    @Test
    void testSendPasswordResetLink() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        authenticationService.sendPasswordResetLink("john@example.com");

        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendPasswordResetEmail(eq("john@example.com"), anyString());
    }

    @Test
    void testResetPassword() {
        String resetToken = "test-token-123";
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userRepository.findByResetToken(resetToken)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encoded-password");

        authenticationService.resetPassword(resetToken, "newPassword123");

        verify(userRepository, times(1)).findByResetToken(resetToken);
        verify(userRepository, times(1)).save(any(User.class));
        assertNull(user.getResetToken());
    }

    @Test
    void testResetPasswordInvalidToken() {
        when(userRepository.findByResetToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.resetPassword("invalid-token", "newPassword123"));
    }

    @Test
    void testResetPasswordExpiredToken() {
        String resetToken = "test-token-123";
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByResetToken(resetToken)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authenticationService.resetPassword(resetToken, "newPassword123"));
    }
}
