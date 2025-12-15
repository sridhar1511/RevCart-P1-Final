package com.revcart.controller;

import com.revcart.config.JwtUtils;
import com.revcart.dto.LoginRequest;
import com.revcart.dto.SignupRequest;
import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import com.revcart.service.UserDetailsImpl;
import com.revcart.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for email: " + loginRequest.getEmail());
            

            
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
            
            if (user == null) {
                System.out.println("User not found for email: " + loginRequest.getEmail());
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
            }
            
            System.out.println("User found: " + user.getName() + ", Verified: " + user.getIsVerified());
            
            // Check password (handle both encoded and plain text)
            boolean passwordMatches = false;
            if (user.getPassword().startsWith("$2a$")) {
                // BCrypt encoded password
                passwordMatches = encoder.matches(loginRequest.getPassword(), user.getPassword());
            } else {
                // Plain text password - encode it and update
                if (loginRequest.getPassword().equals(user.getPassword())) {
                    passwordMatches = true;
                    // Update to encoded password
                    user.setPassword(encoder.encode(user.getPassword()));
                    userRepository.save(user);
                }
            }
            
            if (!passwordMatches) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
            }
            
            if (!user.getIsVerified() && user.getRole() != User.Role.ADMIN) {
                try {
                    authenticationService.sendOtp(loginRequest.getEmail());
                } catch (Exception e) {
                    System.err.println("Failed to send OTP during login: " + e.getMessage());
                }
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email not verified. OTP sent to your email.",
                    "requiresVerification", true,
                    "email", loginRequest.getEmail()
                ));
            }
            
            if (user.getRole() == User.Role.ADMIN) {
                user.setIsVerified(true);
                userRepository.save(user);
            }
            
            // Create UserDetails manually for JWT generation
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            String[] nameParts = userDetails.getName().split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("id", userDetails.getId());
            response.put("firstName", firstName);
            response.put("lastName", lastName);
            response.put("email", userDetails.getUsername());
            response.put("role", user.getRole().toString());
            response.put("profilePicture", user.getProfilePicture());
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Map<String, Object> response = new HashMap<>();
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            response.put("message", "Email is already taken!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User(signUpRequest.getName(),
                           signUpRequest.getEmail(),
                           encoder.encode(signUpRequest.getPassword()));

        user.setPhone(signUpRequest.getPhone());
        user.setAddress(signUpRequest.getAddress());
        user.setIsVerified(false);
        userRepository.save(user);
        
        try {
            authenticationService.sendOtp(signUpRequest.getEmail());
            response.put("message", "User registered successfully! OTP sent to email.");
            response.put("email", signUpRequest.getEmail());
            response.put("requiresVerification", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "User registered but OTP sending failed: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        try {
            authenticationService.sendOtp(email);
            return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            authenticationService.verifyOtp(email, otp);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.sendPasswordResetLink(email);
            return ResponseEntity.ok(Map.of("message", "Password reset link sent to email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            authenticationService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
