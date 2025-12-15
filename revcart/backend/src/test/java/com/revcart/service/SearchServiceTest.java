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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SearchService searchService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Apple", "fruits", new BigDecimal("50.00"), "kg", "apple.jpg", "Fresh apples");
        product.setId(1L);
    }

    @Test
    void testSearchProducts() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.searchProducts("Apple");

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFilterByCategory() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.filterByCategory("fruits");

        assertEquals(1, result.size());
        assertEquals("fruits", result.get(0).getCategory());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFilterByPriceRange() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.filterByPriceRange(new BigDecimal("40.00"), new BigDecimal("60.00"));

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetTrendingProducts() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.getTrendingProducts();

        assertNotNull(result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetRecommendedProducts() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.getRecommendedProducts("fruits");

        assertNotNull(result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testAdvancedSearch() {
        List<Product> products = new ArrayList<>(List.of(product));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = searchService.advancedSearch("Apple", new BigDecimal("40.00"), new BigDecimal("60.00"), "fruits");

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSearchEmpty() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<Product> result = searchService.searchProducts("NonExistent");

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }
}
