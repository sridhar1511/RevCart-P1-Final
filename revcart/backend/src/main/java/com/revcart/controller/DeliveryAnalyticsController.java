package com.revcart.controller;

import com.revcart.service.DeliveryAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery/analytics")
public class DeliveryAnalyticsController {

    @Autowired
    private DeliveryAnalyticsService analyticsService;

    @PostMapping("/update")
    public ResponseEntity<?> updateAnalytics() {
        try {
            analyticsService.updateDailyAnalytics();
            return ResponseEntity.ok(Map.of("success", true, "message", "Analytics updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<?> getAgentDashboard(@PathVariable Long agentId) {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", analyticsService.getAgentDashboard(agentId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/performance")
    public ResponseEntity<?> getPerformance(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(Map.of("success", true, "data", analyticsService.getAgentPerformance(start, end)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/top-agents")
    public ResponseEntity<?> getTopAgents(@RequestParam(required = false) String date) {
        try {
            LocalDate queryDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            return ResponseEntity.ok(Map.of("success", true, "data", analyticsService.getTopAgents(queryDate)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/daily-stats")
    public ResponseEntity<?> getDailyStats() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", analyticsService.getDailyStats()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics() {
        try {
            return ResponseEntity.ok(Map.of("success", true, "data", analyticsService.getDeliveryMetrics()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
