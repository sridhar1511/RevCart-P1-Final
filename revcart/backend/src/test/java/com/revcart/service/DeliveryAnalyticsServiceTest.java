package com.revcart.service;

import com.revcart.document.DeliveryAnalytics;
import com.revcart.document.DeliveryLog;
import com.revcart.entity.DeliveryAgent;
import com.revcart.mongo.DeliveryAnalyticsRepository;
import com.revcart.mongo.DeliveryLogRepository;
import com.revcart.repository.DeliveryAgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryAnalyticsServiceTest {

    @Mock
    private DeliveryAnalyticsRepository analyticsRepository;

    @Mock
    private DeliveryLogRepository deliveryLogRepository;

    @Mock
    private DeliveryAgentRepository agentRepository;

    @InjectMocks
    private DeliveryAnalyticsService deliveryAnalyticsService;

    private DeliveryAgent agent;
    private DeliveryAnalytics analytics;
    private DeliveryLog deliveryLog;

    @BeforeEach
    void setUp() {
        agent = new DeliveryAgent();
        agent.setId(1L);
        agent.setName("John Agent");

        analytics = new DeliveryAnalytics(1L, "John Agent", LocalDate.now());
        analytics.setTotalDeliveries(10);
        analytics.setSuccessfulDeliveries(8);
        analytics.setFailedDeliveries(2);

        deliveryLog = new DeliveryLog();
        deliveryLog.setStatus("DELIVERED");
    }

    @Test
    void testUpdateDailyAnalytics() {
        List<DeliveryAgent> agents = new ArrayList<>(List.of(agent));
        List<DeliveryLog> logs = new ArrayList<>(List.of(deliveryLog));

        when(agentRepository.findAll()).thenReturn(agents);
        when(analyticsRepository.findByAgentIdAndDate(1L, LocalDate.now())).thenReturn(Optional.of(analytics));
        when(deliveryLogRepository.findByDeliveryAgentIdAndDate(1L, LocalDate.now())).thenReturn(logs);

        deliveryAnalyticsService.updateDailyAnalytics();

        verify(agentRepository, times(1)).findAll();
        verify(analyticsRepository, times(1)).save(any(DeliveryAnalytics.class));
    }

    @Test
    void testGetAgentDashboard() {
        List<DeliveryAnalytics> weekStats = new ArrayList<>(List.of(analytics));
        when(analyticsRepository.findByAgentIdAndDate(1L, LocalDate.now())).thenReturn(Optional.of(analytics));
        when(analyticsRepository.findByAgentId(1L)).thenReturn(weekStats);

        Map<String, Object> result = deliveryAnalyticsService.getAgentDashboard(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("todayStats"));
        assertTrue(result.containsKey("weekStats"));
        assertTrue(result.containsKey("totalDeliveries"));
        assertTrue(result.containsKey("successRate"));
        verify(analyticsRepository, times(1)).findByAgentIdAndDate(1L, LocalDate.now());
    }

    @Test
    void testGetAgentPerformance() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Object> performance = new ArrayList<>();

        when(analyticsRepository.getAgentPerformance(startDate, endDate)).thenReturn(performance);

        List<Object> result = deliveryAnalyticsService.getAgentPerformance(startDate, endDate);

        assertNotNull(result);
        verify(analyticsRepository, times(1)).getAgentPerformance(startDate, endDate);
    }

    @Test
    void testGetTopAgents() {
        List<DeliveryAnalytics> topAgents = new ArrayList<>(List.of(analytics));
        when(analyticsRepository.getTopAgentsByDate(LocalDate.now())).thenReturn(topAgents);

        List<DeliveryAnalytics> result = deliveryAnalyticsService.getTopAgents(LocalDate.now());

        assertEquals(1, result.size());
        verify(analyticsRepository, times(1)).getTopAgentsByDate(LocalDate.now());
    }

    @Test
    void testGetDailyStats() {
        List<Object> stats = new ArrayList<>();
        when(analyticsRepository.getDailyDeliveryStats()).thenReturn(stats);

        List<Object> result = deliveryAnalyticsService.getDailyStats();

        assertNotNull(result);
        verify(analyticsRepository, times(1)).getDailyDeliveryStats();
    }

    @Test
    void testGetDeliveryMetrics() {
        List<DeliveryAnalytics> todayAnalytics = new ArrayList<>(List.of(analytics));
        when(analyticsRepository.findByDate(LocalDate.now())).thenReturn(todayAnalytics);

        Map<String, Object> result = deliveryAnalyticsService.getDeliveryMetrics();

        assertNotNull(result);
        assertTrue(result.containsKey("totalDeliveries"));
        assertTrue(result.containsKey("successfulDeliveries"));
        assertTrue(result.containsKey("failedDeliveries"));
        assertTrue(result.containsKey("averageRating"));
        assertTrue(result.containsKey("activeAgents"));
        verify(analyticsRepository, times(1)).findByDate(LocalDate.now());
    }
}
