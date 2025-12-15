package com.revcart.service;

import com.revcart.entity.*;
import com.revcart.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123");
        user.setId(1L);

        product = new Product("Apple", "fruits", new BigDecimal("50.00"), "kg", "apple.jpg", "Fresh apples");
        product.setId(1L);

        cart = new Cart(user);
        cart.setId(1L);
        cart.setCartItems(new ArrayList<>());
    }

    @Test
    void testGetOrCreateCartExisting() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUser(user);
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testGetOrCreateCartNew() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getOrCreateCart(user);

        assertNotNull(result);
        verify(cartRepository, times(1)).findByUser(user);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.addToCart(user, 1L, 2);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).findById(1L);
    }

    @Test
    void testAddToCartProductNotFound() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addToCart(user, 999L, 2));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateCartItem() {
        CartItem cartItem = new CartItem(cart, product, 1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartItem));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.updateCartItem(user, 1L, 5);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveFromCart() {
        CartItem cartItem = new CartItem(cart, product, 1);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartItem));

        cartService.removeFromCart(user, 1L);

        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testClearCart() {
        CartItem cartItem = new CartItem(cart, product, 1);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.clearCart(user);

        verify(cartItemRepository, times(1)).deleteAll(any());
        verify(cartItemRepository, times(1)).deleteByCart(cart);
    }

    @Test
    void testGetCart() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCart(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUser(user);
    }
}
