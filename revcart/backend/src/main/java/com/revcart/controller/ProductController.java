package com.revcart.controller;

import com.revcart.dto.ProductRequest;
import com.revcart.entity.Product;
import com.revcart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(new BigDecimal(request.getPrice()));
            product.setCategory(request.getCategory());
            product.setStockQuantity(request.getStockQuantity());
            product.setImage(request.getImage());
            product.setUnit(request.getUnit());
            
            Product created = productService.createProduct(product);
            return ResponseEntity.ok(Map.of("message", "Product created successfully", "product", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating product: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        try {
            Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
            
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(new BigDecimal(request.getPrice()));
            product.setCategory(request.getCategory());
            product.setStockQuantity(request.getStockQuantity());
            if (request.getImage() != null) product.setImage(request.getImage());
            if (request.getUnit() != null) product.setUnit(request.getUnit());
            
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(Map.of("message", "Product updated successfully", "product", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting product: " + e.getMessage()));
        }
    }
}
