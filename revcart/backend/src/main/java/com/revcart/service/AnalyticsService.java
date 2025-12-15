package com.revcart.service;

import com.revcart.repository.OrderRepository;
import com.revcart.repository.PaymentRepository;
import com.revcart.repository.UserRepository;
import com.revcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", orderRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalProducts", productRepository.count());
        stats.put("totalRevenue", calculateTotalRevenue());
        stats.put("todayOrders", countOrdersToday());
        stats.put("todayRevenue", calculateTodayRevenue());
        stats.put("pendingOrders", countPendingOrders());
        stats.put("deliveredOrders", countDeliveredOrders());
        
        return stats;
    }

    public Map<String, Object> getSalesAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        analytics.put("dailySales", calculateDailySales());
        analytics.put("monthlySales", calculateMonthlySales());
        analytics.put("topProducts", getTopProducts());
        analytics.put("topCategories", getTopCategories());
        
        return analytics;
    }

    public Map<String, Object> getUserAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        analytics.put("totalUsers", userRepository.count());
        analytics.put("activeUsers", countActiveUsers());
        analytics.put("newUsersThisMonth", countNewUsersThisMonth());
        analytics.put("userRetention", calculateUserRetention());
        
        return analytics;
    }

    private Double calculateTotalRevenue() {
        return orderRepository.findAll().stream()
            .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0)
            .sum();
    }

    private Double calculateTodayRevenue() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        return orderRepository.findAll().stream()
            .filter(order -> order.getOrderDate() != null && 
                    order.getOrderDate().isAfter(startOfDay) && 
                    order.getOrderDate().isBefore(endOfDay))
            .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0)
            .sum();
    }

    private Long countOrdersToday() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        return orderRepository.findAll().stream()
            .filter(order -> order.getOrderDate() != null && 
                    order.getOrderDate().isAfter(startOfDay) && 
                    order.getOrderDate().isBefore(endOfDay))
            .count();
    }

    private Long countPendingOrders() {
        return orderRepository.findAll().stream()
            .filter(order -> "PENDING".equals(order.getStatus()))
            .count();
    }

    private Long countDeliveredOrders() {
        return orderRepository.findAll().stream()
            .filter(order -> "DELIVERED".equals(order.getStatus()))
            .count();
    }

    private Long countActiveUsers() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return userRepository.findAll().stream()
            .filter(user -> user.getCreatedAt() != null && user.getCreatedAt().isAfter(oneMonthAgo))
            .count();
    }

    private Long countNewUsersThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        return userRepository.findAll().stream()
            .filter(user -> user.getCreatedAt() != null && user.getCreatedAt().isAfter(startOfMonth))
            .count();
    }

    private Map<String, Long> calculateDailySales() {
        Map<String, Long> dailySales = new HashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
            
            long sales = orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null && 
                        order.getOrderDate().isAfter(startOfDay) && 
                        order.getOrderDate().isBefore(endOfDay))
                .count();
            
            dailySales.put(date.toString(), sales);
        }
        return dailySales;
    }

    private Map<String, Long> calculateMonthlySales() {
        Map<String, Long> monthlySales = new HashMap<>();
        for (int i = 11; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            String month = date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            
            long sales = orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null && 
                        order.getOrderDate().getYear() == date.getYear() &&
                        order.getOrderDate().getMonthValue() == date.getMonthValue())
                .count();
            
            monthlySales.put(month, sales);
        }
        return monthlySales;
    }

    private Map<String, Long> getTopProducts() {
        Map<String, Long> topProducts = new HashMap<>();
        return topProducts;
    }

    private Map<String, Long> getTopCategories() {
        Map<String, Long> topCategories = new HashMap<>();
        return topCategories;
    }

    private Double calculateUserRetention() {
        long totalUsers = userRepository.count();
        long activeUsers = countActiveUsers();
        return totalUsers > 0 ? (activeUsers * 100.0) / totalUsers : 0;
    }
}
