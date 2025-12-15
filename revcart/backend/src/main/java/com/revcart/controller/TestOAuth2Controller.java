package com.revcart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/oauth2")
@CrossOrigin(origins = "http://localhost:4200")
public class TestOAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @GetMapping("/config")
    public ResponseEntity<?> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("clientId", clientId);
        config.put("status", clientId.equals("YOUR_GOOGLE_CLIENT_ID") ? "NOT_CONFIGURED" : "CONFIGURED");
        config.put("message", clientId.equals("YOUR_GOOGLE_CLIENT_ID") ? 
            "Google credentials not configured. Update application.properties" : 
            "Google credentials configured");
        return ResponseEntity.ok(config);
    }
}
