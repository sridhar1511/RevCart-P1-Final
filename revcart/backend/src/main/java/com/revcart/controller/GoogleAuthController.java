package com.revcart.controller;

import com.revcart.config.JwtUtils;
import com.revcart.entity.User;
import com.revcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
@CrossOrigin(origins = "http://localhost:4200")
public class GoogleAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @PostMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            
            RestTemplate restTemplate = new RestTemplate();
            String tokenUrl = "https://oauth2.googleapis.com/token";
            
            Map<String, String> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            params.put("redirect_uri", "http://localhost:4200/auth/google-callback");
            params.put("grant_type", "authorization_code");
            
            Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, params, Map.class);
            String accessToken = (String) tokenResponse.get("access_token");
            
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
            Map<String, Object> userInfo = restTemplate.getForObject(userInfoUrl, Map.class);
            
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            String picture = (String) userInfo.get("picture");
            
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setProfilePicture(picture);
                user.setPassword(passwordEncoder.encode("oauth2-" + System.currentTimeMillis()));
                user.setRole(User.Role.USER);
                user.setIsVerified(true);
                user.setPhone("");
                userRepository.save(user);
            } else {
                user.setProfilePicture(picture);
                userRepository.save(user);
            }
            
            String jwtToken = jwtUtils.generateJwtTokenFromEmail(email);
            String[] nameParts = name.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("type", "Bearer");
            response.put("id", user.getId());
            response.put("firstName", firstName);
            response.put("lastName", lastName);
            response.put("email", email);
            response.put("role", user.getRole().toString());
            response.put("profilePicture", picture);
            response.put("message", "Google login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Google authentication failed: " + e.getMessage()));
        }
    }
}
