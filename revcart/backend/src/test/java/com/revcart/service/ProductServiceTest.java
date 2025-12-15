package com.revcart.service;

import com.revcart.entity.Product;
import com.revcart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Apple", "fruits", new BigDecimal("50.00"), "kg", "apple.jpg", "Fresh apples");
        product.setId(1L);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Apple", result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(999L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testGetProductsByCategory() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByCategory("fruits")).thenReturn(products);

        List<Product> result = productService.getProductsByCategory("fruits");

        assertEquals(1, result.size());
        assertEquals("fruits", result.get(0).getCategory());
        verify(productRepository, times(1)).findByCategory("fruits");
    }

    @Test
    void testSearchProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByNameContainingIgnoreCase("Apple")).thenReturn(products);

        List<Product> result = productService.searchProducts("Apple");

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Apple");
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product("Red Apple", "fruits", new BigDecimal("60.00"), "kg", "red_apple.jpg", "Fresh red apples");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Red Apple", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateProduct(999L, product));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllCategories() {
        List<Product> products = Arrays.asList(
            new Product("Apple", "fruits", new BigDecimal("50.00"), "kg", "apple.jpg", "Fresh apples"),
            new Product("Carrot", "vegetables", new BigDecimal("30.00"), "kg", "carrot.jpg", "Fresh carrots")
        );
        when(productRepository.findAll()).thenReturn(products);

        List<String> result = productService.getAllCategories();

        assertEquals(2, result.size());
        assertTrue(result.contains("fruits"));
        assertTrue(result.contains("vegetables"));
        verify(productRepository, times(1)).findAll();
    }
}
