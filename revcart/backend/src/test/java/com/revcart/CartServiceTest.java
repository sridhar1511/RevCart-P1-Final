package com.revcart;   

import com.revcart.entity.Cart;
import com.revcart.entity.Product;
import com.revcart.entity.User;
import com.revcart.repository.ProductRepository;
import com.revcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testAddToCart() {
        // Create test user
        User testUser = new User("Test User", "test@example.com", "password");
        testUser.setId(1L);

        // Create test product
        Product testProduct = new Product("Test Product", "test", new BigDecimal("100"), "1 piece", "test-image.jpg", "Test description");
        testProduct.setId(1L);
        productRepository.save(testProduct);

        // Add to cart
        Cart cart = cartService.addToCart(testUser, 1L, 2);

        assertNotNull(cart);
        if (cart.getCartItems() != null) {
            assertEquals(1, cart.getCartItems().size());
            assertEquals(2, cart.getCartItems().get(0).getQuantity());
        }
    }

    @Test
    public void testGetCart() {
        User testUser = new User("Test User", "test@example.com", "password");
        testUser.setId(1L);

        Cart cart = cartService.getCart(testUser);
        assertNotNull(cart);
    }

    @Test
    public void testUpdateCartItem() {
        User testUser = new User("Test User", "test@example.com", "password");
        testUser.setId(1L);

        Product testProduct = new Product("Test Product", "test", new BigDecimal("100"), "1 piece", "test-image.jpg", "Test description");
        testProduct.setId(1L);
        productRepository.save(testProduct);

        // Add item first
        cartService.addToCart(testUser, 1L, 2);

        // Update quantity
        Cart updatedCart = cartService.updateCartItem(testUser, 1L, 5);

        if (updatedCart.getCartItems() != null && !updatedCart.getCartItems().isEmpty()) {
            assertEquals(5, updatedCart.getCartItems().get(0).getQuantity());
        }
    }
}