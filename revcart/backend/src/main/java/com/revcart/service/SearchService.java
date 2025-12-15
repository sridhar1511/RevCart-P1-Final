package com.revcart.service;

import com.revcart.entity.Product;
import com.revcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchProducts(String query) {
        return productRepository.findAll().stream()
            .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()) ||
                        p.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                        p.getDescription().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
            .filter(p -> p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0)
            .collect(Collectors.toList());
    }

    public List<Product> filterByCategory(String category) {
        return productRepository.findAll().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    public List<Product> getTrendingProducts() {
        return productRepository.findAll().stream()
            .limit(10)
            .collect(Collectors.toList());
    }

    public List<Product> getRecommendedProducts(String category) {
        return productRepository.findAll().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .limit(5)
            .collect(Collectors.toList());
    }

    public List<Product> advancedSearch(String query, BigDecimal minPrice, BigDecimal maxPrice, String category) {
        return productRepository.findAll().stream()
            .filter(p -> query == null || p.getName().toLowerCase().contains(query.toLowerCase()) ||
                        p.getCategory().toLowerCase().contains(query.toLowerCase()))
            .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
            .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
            .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }
}
