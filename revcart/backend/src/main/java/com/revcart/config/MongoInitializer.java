package com.revcart.config;

import com.revcart.document.OrderAnalytics;
import com.revcart.mongo.OrderAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoInitializer implements CommandLineRunner {

    @Autowired
    private OrderAnalyticsRepository analyticsRepository;

    @Override
    public void run(String... args) throws Exception {
        if (analyticsRepository.count() == 0) {
            initializeAnalytics();
        }
    }

    private void initializeAnalytics() {
        List<OrderAnalytics> analytics = new ArrayList<>();
        
        analytics.add(new OrderAnalytics(1L, "Fresh Apples", "fruits", 45, new BigDecimal("5400.00")));
        analytics.add(new OrderAnalytics(6L, "Tomatoes", "vegetables", 38, new BigDecimal("3040.00")));
        analytics.add(new OrderAnalytics(9L, "Milk", "dairy", 52, new BigDecimal("2860.00")));
        analytics.add(new OrderAnalytics(13L, "Bread", "bakery", 35, new BigDecimal("1225.00")));
        analytics.add(new OrderAnalytics(17L, "Smartphone", "electronics", 12, new BigDecimal("180000.00")));
        analytics.add(new OrderAnalytics(21L, "Football", "sports", 28, new BigDecimal("22400.00")));
        analytics.add(new OrderAnalytics(2L, "Bananas", "fruits", 42, new BigDecimal("2520.00")));
        analytics.add(new OrderAnalytics(7L, "Onions", "vegetables", 55, new BigDecimal("1650.00")));
        analytics.add(new OrderAnalytics(10L, "Cheese", "dairy", 18, new BigDecimal("3600.00")));
        analytics.add(new OrderAnalytics(14L, "Croissant", "bakery", 48, new BigDecimal("1200.00")));
        
        analyticsRepository.saveAll(analytics);
    }
}
