package com.revcart.controller;

import com.revcart.entity.Product;
import com.revcart.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String query) {
        try {
            List<Product> products = searchService.searchProducts(query);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error searching products"));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> advancedSearch(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String category) {
        try {
            List<Product> products = searchService.advancedSearch(query, minPrice, maxPrice, category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error filtering products"));
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingProducts() {
        try {
            List<Product> products = searchService.getTrendingProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching trending products"));
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam String category) {
        try {
            List<Product> products = searchService.getRecommendedProducts(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching recommendations"));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable String category) {
        try {
            List<Product> products = searchService.filterByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching products by category"));
        }
    }
}
