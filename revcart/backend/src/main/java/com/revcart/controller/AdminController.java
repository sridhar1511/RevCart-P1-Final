package com.revcart.controller;

import com.revcart.entity.Order;
import com.revcart.entity.Product;
import com.revcart.entity.User;
import com.revcart.repository.OrderRepository;
import com.revcart.repository.ProductRepository;
import com.revcart.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching users"));
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(Map.of("message", "Product added successfully", "product", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error adding product"));
        }
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody Product productUpdate) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
            
            product.setName(productUpdate.getName());
            product.setCategory(productUpdate.getCategory());
            product.setPrice(productUpdate.getPrice());
            product.setUnit(productUpdate.getUnit());
            product.setImage(productUpdate.getImage());
            product.setDescription(productUpdate.getDescription());
            product.setStockQuantity(productUpdate.getStockQuantity());
            
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(Map.of("message", "Product updated successfully", "product", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating product"));
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productRepository.deleteById(productId);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting product"));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching orders"));
        }
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam Order.OrderStatus status) {
        try {
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
            
            order.setStatus(status);
            Order savedOrder = orderRepository.save(order);
            
            return ResponseEntity.ok(Map.of("message", "Order status updated", "order", savedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating order status"));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        try {
            long totalUsers = userRepository.count();
            long totalProducts = productRepository.count();
            long totalOrders = orderRepository.count();
            long pendingOrders = orderRepository.findByStatusOrderByOrderDateDesc(Order.OrderStatus.PENDING).size();
            
            return ResponseEntity.ok(Map.of(
                "totalUsers", totalUsers,
                "totalProducts", totalProducts,
                "totalOrders", totalOrders,
                "pendingOrders", pendingOrders
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching dashboard stats"));
        }
    }
}