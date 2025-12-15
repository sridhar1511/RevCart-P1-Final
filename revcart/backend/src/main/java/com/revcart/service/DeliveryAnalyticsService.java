package com.revcart.service;

import com.revcart.document.DeliveryAnalytics;
import com.revcart.document.DeliveryLog;
import com.revcart.entity.DeliveryAgent;
import com.revcart.mongo.DeliveryAnalyticsRepository;
import com.revcart.mongo.DeliveryLogRepository;
import com.revcart.repository.DeliveryAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DeliveryAnalyticsService {

    @Autowired
    private DeliveryAnalyticsRepository analyticsRepository;

    @Autowired
    private DeliveryLogRepository deliveryLogRepository;

    @Autowired
    private DeliveryAgentRepository agentRepository;

    public void updateDailyAnalytics() {
        LocalDate today = LocalDate.now();
        List<DeliveryAgent> agents = agentRepository.findAll();
        
        for (DeliveryAgent agent : agents) {
            DeliveryAnalytics analytics = analyticsRepository.findByAgentIdAndDate(agent.getId(), today)
                .orElse(new DeliveryAnalytics(agent.getId(), agent.getName(), today));
            
            List<DeliveryLog> logs = deliveryLogRepository.findByDeliveryAgentIdAndDate(agent.getId(), today);
            
            analytics.setTotalDeliveries(logs.size());
            analytics.setSuccessfulDeliveries((int) logs.stream().filter(l -> "DELIVERED".equals(l.getStatus())).count());
            analytics.setFailedDeliveries((int) logs.stream().filter(l -> "FAILED".equals(l.getStatus())).count());
            analytics.setLastUpdated(System.currentTimeMillis());
            
            analyticsRepository.save(analytics);
        }
    }

    public Map<String, Object> getAgentDashboard(Long agentId) {
        Map<String, Object> dashboard = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        DeliveryAnalytics todayStats = analyticsRepository.findByAgentIdAndDate(agentId, today)
            .orElse(new DeliveryAnalytics(agentId, "", today));
        
        List<DeliveryAnalytics> weekStats = analyticsRepository.findByAgentId(agentId).stream()
            .filter(a -> a.getDate().isAfter(today.minusDays(7)))
            .toList();
        
        dashboard.put("todayStats", todayStats);
        dashboard.put("weekStats", weekStats);
        dashboard.put("totalDeliveries", weekStats.stream().mapToInt(DeliveryAnalytics::getTotalDeliveries).sum());
        dashboard.put("successRate", calculateSuccessRate(weekStats));
        
        return dashboard;
    }

    public List<Object> getAgentPerformance(LocalDate startDate, LocalDate endDate) {
        return analyticsRepository.getAgentPerformance(startDate, endDate);
    }

    public List<DeliveryAnalytics> getTopAgents(LocalDate date) {
        return analyticsRepository.getTopAgentsByDate(date);
    }

    public List<Object> getDailyStats() {
        return analyticsRepository.getDailyDeliveryStats();
    }

    public Map<String, Object> getDeliveryMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        List<DeliveryAnalytics> todayAnalytics = analyticsRepository.findByDate(today);
        
        metrics.put("totalDeliveries", todayAnalytics.stream().mapToInt(DeliveryAnalytics::getTotalDeliveries).sum());
        metrics.put("successfulDeliveries", todayAnalytics.stream().mapToInt(DeliveryAnalytics::getSuccessfulDeliveries).sum());
        metrics.put("failedDeliveries", todayAnalytics.stream().mapToInt(DeliveryAnalytics::getFailedDeliveries).sum());
        metrics.put("averageRating", todayAnalytics.stream().mapToDouble(DeliveryAnalytics::getAverageRating).average().orElse(0.0));
        metrics.put("activeAgents", todayAnalytics.size());
        
        return metrics;
    }

    private Double calculateSuccessRate(List<DeliveryAnalytics> stats) {
        if (stats.isEmpty()) return 0.0;
        int total = stats.stream().mapToInt(DeliveryAnalytics::getTotalDeliveries).sum();
        int successful = stats.stream().mapToInt(DeliveryAnalytics::getSuccessfulDeliveries).sum();
        return total > 0 ? (successful * 100.0) / total : 0.0;
    }
}
