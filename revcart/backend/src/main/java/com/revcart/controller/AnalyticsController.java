package com.revcart.controller;

import com.revcart.service.AnalyticsService;
import com.revcart.service.MongoAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private MongoAnalyticsService mongoAnalyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Object> stats = analyticsService.getDashboardStats();
            return ResponseEntity.ok(Map.of("success", true, "data", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getSalesAnalytics() {
        try {
            Map<String, Object> analytics = analyticsService.getSalesAnalytics();
            return ResponseEntity.ok(Map.of("success", true, "data", analytics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserAnalytics() {
        try {
            Map<String, Object> analytics = analyticsService.getUserAnalytics();
            return ResponseEntity.ok(Map.of("success", true, "data", analytics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/update-analytics")
    public ResponseEntity<?> updateAnalytics() {
        try {
            mongoAnalyticsService.updateAnalytics();
            return ResponseEntity.ok(Map.of("success", true, "message", "Analytics updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/most-purchased")
    public ResponseEntity<?> getMostPurchasedItems() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", mongoAnalyticsService.getMostPurchasedItems()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/top-categories")
    public ResponseEntity<?> getTopCategories() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", mongoAnalyticsService.getTopCategoriesByRevenue()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getAnalyticsSummary() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", mongoAnalyticsService.getAnalyticsSummary()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
