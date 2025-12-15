package com.revcart.service;

import com.revcart.document.OrderAnalytics;
import com.revcart.entity.Order;
import com.revcart.entity.OrderItem;
import com.revcart.mongo.OrderAnalyticsRepository;
import com.revcart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MongoAnalyticsService {

    @Autowired
    private OrderAnalyticsRepository analyticsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateAnalytics() {
        Map<Long, OrderAnalytics> analyticsMap = new HashMap<>();
        
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                Long productId = item.getProduct().getId();
                analyticsMap.putIfAbsent(productId, new OrderAnalytics(
                    productId,
                    item.getProduct().getName(),
                    item.getProduct().getCategory(),
                    0,
                    BigDecimal.ZERO
                ));
                
                OrderAnalytics analytics = analyticsMap.get(productId);
                analytics.setPurchaseCount(analytics.getPurchaseCount() + item.getQuantity());
                analytics.setTotalRevenue(analytics.getTotalRevenue().add(
                    item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ));
            }
        }
        
        analyticsRepository.deleteAll();
        analyticsRepository.saveAll(analyticsMap.values());
    }

    public List<OrderAnalytics> getMostPurchasedItems() {
        return analyticsRepository.findTopPurchasedItems();
    }

    public List<OrderAnalytics> getTopItemsByCategory(String category) {
        return analyticsRepository.findTopItemsByCategory(category);
    }

    public List<Object> getTopCategoriesByRevenue() {
        return analyticsRepository.findTopCategoriesByRevenue();
    }

    public Map<String, Object> getAnalyticsSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("mostPurchasedItems", getMostPurchasedItems());
        summary.put("topCategories", getTopCategoriesByRevenue());
        return summary;
    }
}
