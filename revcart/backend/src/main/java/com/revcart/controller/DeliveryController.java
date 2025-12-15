package com.revcart.controller;

import com.revcart.entity.DeliveryAgent;
import com.revcart.entity.Order;
import com.revcart.repository.DeliveryAgentRepository;
import com.revcart.service.DeliveryService;
import com.revcart.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "http://localhost:4200")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/agent/register")
    public ResponseEntity<?> agentRegister(@RequestParam String email, @RequestParam String password, 
                                          @RequestParam String name, @RequestParam String phone) {
        try {
            if (deliveryAgentRepository.findAll().stream().anyMatch(a -> a.getEmail().equals(email))) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
            }
            DeliveryAgent agent = new DeliveryAgent();
            agent.setEmail(email);
            agent.setPassword(passwordEncoder.encode(password));
            agent.setName(name);
            agent.setPhone(phone);
            agent.setStatus(DeliveryAgent.Status.AVAILABLE);
            agent.setIsVerified(false);
            deliveryAgentRepository.save(agent);
            
            String otp = generateOtp();
            agent.setOtp(otp);
            agent.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            deliveryAgentRepository.save(agent);
            
            try {
                emailService.sendOtpEmail(email, otp);
            } catch (Exception e) {
                System.err.println("Failed to send OTP: " + e.getMessage());
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Registration successful! OTP sent to email.",
                "email", email,
                "requiresVerification", true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/agent/login")
    public ResponseEntity<?> agentLogin(@RequestParam String email, @RequestParam String password) {
        try {
            DeliveryAgent agent = deliveryAgentRepository.findAll().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
            
            // Check password - handle both plain text (test agent) and encrypted passwords
            boolean passwordMatches = false;
            if (agent.getPassword().equals(password)) {
                // Plain text password match (for test agent)
                passwordMatches = true;
            } else {
                // Encrypted password match
                passwordMatches = passwordEncoder.matches(password, agent.getPassword());
            }
            
            if (!passwordMatches) {
                throw new RuntimeException("Invalid credentials");
            }
            
            if (!agent.getIsVerified()) {
                String otp = generateOtp();
                agent.setOtp(otp);
                agent.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
                deliveryAgentRepository.save(agent);
                
                try {
                    System.out.println("[DeliveryController] Sending login OTP to: " + email);
                    emailService.sendOtpEmail(email, otp);
                    System.out.println("[DeliveryController] Login OTP sent successfully");
                } catch (Exception e) {
                    System.err.println("[DeliveryController] Failed to send login OTP: " + e.getMessage());
                    e.printStackTrace();
                }
                
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email not verified. OTP sent to your email.",
                    "requiresVerification", true,
                    "email", email
                ));
            }
            
            return ResponseEntity.ok(Map.of("message", "Login successful", "agent", agent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/agent/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            DeliveryAgent agent = deliveryAgentRepository.findAll().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Agent not found"));
            
            if (!agent.getOtp().equals(otp)) {
                throw new RuntimeException("Invalid OTP");
            }
            
            if (LocalDateTime.now().isAfter(agent.getOtpExpiry())) {
                throw new RuntimeException("OTP expired");
            }
            
            agent.setIsVerified(true);
            agent.setOtp(null);
            agent.setOtpExpiry(null);
            deliveryAgentRepository.save(agent);
            
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/agent/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        try {
            DeliveryAgent agent = deliveryAgentRepository.findAll().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Agent not found"));
            
            String otp = generateOtp();
            agent.setOtp(otp);
            agent.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            deliveryAgentRepository.save(agent);
            
            try {
                System.out.println("[DeliveryController] Resending OTP to: " + email);
                emailService.sendOtpEmail(email, otp);
                System.out.println("[DeliveryController] OTP resent successfully");
            } catch (Exception e) {
                System.err.println("[DeliveryController] Failed to resend OTP: " + e.getMessage());
                e.printStackTrace();
            }
            
            return ResponseEntity.ok(Map.of("message", "OTP resent to email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/available-agents")
    public ResponseEntity<?> getAvailableAgents() {
        try {
            List<DeliveryAgent> agents = deliveryService.getAvailableAgents();
            return ResponseEntity.ok(agents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching agents"));
        }
    }

    @PostMapping("/assign/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignDeliveryAgent(@PathVariable Long orderId) {
        try {
            DeliveryAgent agent = deliveryService.assignDeliveryAgent(orderId);
            return ResponseEntity.ok(Map.of("message", "Agent assigned", "agent", agent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Long orderId, @RequestParam Order.OrderStatus status) {
        try {
            deliveryService.updateDeliveryStatus(orderId, status);
            return ResponseEntity.ok(Map.of("message", "Status updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating status"));
        }
    }

    @PutMapping("/agent/{agentId}/status")
    public ResponseEntity<?> updateAgentStatus(@PathVariable Long agentId, @RequestParam DeliveryAgent.Status status) {
        try {
            deliveryService.updateAgentStatus(agentId, status);
            return ResponseEntity.ok(Map.of("message", "Agent status updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating agent status"));
        }
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<?> getAgent(@PathVariable Long agentId) {
        try {
            DeliveryAgent agent = deliveryService.getAgentById(agentId);
            return ResponseEntity.ok(agent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching agent"));
        }
    }

    @PostMapping("/location/update")
    public ResponseEntity<?> updateDeliveryLocation(@RequestParam Double latitude, @RequestParam Double longitude) {
        try {
            deliveryService.updateAgentLocation(latitude, longitude);
            return ResponseEntity.ok(Map.of("message", "Location updated", "latitude", latitude, "longitude", longitude));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating location"));
        }
    }

    @GetMapping("/location/{orderId}")
    public ResponseEntity<?> getDeliveryLocation(@PathVariable Long orderId) {
        try {
            Map<String, Object> location = deliveryService.getDeliveryLocation(orderId);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching location"));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getDeliveryOrders() {
        try {
            List<Order> orders = deliveryService.getDeliveryOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching orders"));
        }
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
