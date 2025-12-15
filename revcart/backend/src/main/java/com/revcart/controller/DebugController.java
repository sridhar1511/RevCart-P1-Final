package com.revcart.controller;

import com.revcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "http://localhost:4200")
public class DebugController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product-count")
    public ResponseEntity<?> getProductCount() {
        long count = productRepository.count();
        Map<String, Object> response = new HashMap<>();
        response.put("productCount", count);
        response.put("status", count > 0 ? "Products loaded" : "No products found");
        return ResponseEntity.ok(response);
    }
}
