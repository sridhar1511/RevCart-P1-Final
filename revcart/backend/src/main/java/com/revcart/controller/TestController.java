package com.revcart.controller;

import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public Map<String, Object> checkAdmin() {
        Optional<User> admin = userRepository.findByEmail("saithota1207@gmail.com");
        if (admin.isPresent()) {
            User user = admin.get();
            boolean passwordMatches = passwordEncoder.matches("admin123", user.getPassword());
            return Map.of(
                "exists", true,
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().toString(),
                "passwordMatches", passwordMatches
            );
        }
        return Map.of("exists", false);
    }

    @PostMapping("/create-admin")
    public Map<String, String> createAdmin() {
        if (userRepository.existsByEmail("saithota1207@gmail.com")) {
            return Map.of("message", "Admin already exists");
        }
        
        User admin = new User("Sai Thota", "saithota1207@gmail.com", passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        admin.setPhone("9000795258");
        admin.setAddress("Chennai, India");
        userRepository.save(admin);
        
        return Map.of("message", "Admin created successfully");
    }
}