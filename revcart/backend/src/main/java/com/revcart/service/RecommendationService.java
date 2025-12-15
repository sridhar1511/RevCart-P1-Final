package com.revcart.service;

import com.revcart.entity.Order;
import com.revcart.entity.OrderItem;
import com.revcart.entity.Product;
import com.revcart.entity.User;
import com.revcart.repository.OrderRepository;
import com.revcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getRecommendationsForUser(User user) {
        List<Order> userOrders = orderRepository.findByUser(user);
        
        Set<String> purchasedCategories = userOrders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .map(item -> item.getProduct().getCategory())
            .collect(Collectors.toSet());

        return productRepository.findAll().stream()
            .filter(p -> purchasedCategories.contains(p.getCategory()))
            .limit(10)
            .collect(Collectors.toList());
    }

    public List<Product> getPopularProducts() {
        Map<Long, Integer> productFrequency = new HashMap<>();
        
        orderRepository.findAll().forEach(order -> 
            order.getOrderItems().forEach(item -> 
                productFrequency.merge(item.getProduct().getId(), 1, Integer::sum)
            )
        );

        return productFrequency.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(10)
            .map(entry -> productRepository.findById(entry.getKey()).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public List<Product> getSimilarProducts(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return new ArrayList<>();

        return productRepository.findAll().stream()
            .filter(p -> p.getCategory().equals(product.getCategory()) && !p.getId().equals(productId))
            .limit(5)
            .collect(Collectors.toList());
    }
}
