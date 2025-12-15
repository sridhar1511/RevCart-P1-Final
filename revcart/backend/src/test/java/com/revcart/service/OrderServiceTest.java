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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private Cart cart;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123");
        user.setId(1L);

        product = new Product("Apple", "fruits", new BigDecimal("50.00"), "kg", "apple.jpg", "Fresh apples");
        product.setId(1L);

        cart = new Cart(user);
        cart.setId(1L);
        CartItem cartItem = new CartItem(cart, product, 2);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        order = new Order(user, new BigDecimal("100.00"), "123 Main St", "9876543210");
        order.setId(1L);
    }

    @Test
    void testCreateOrder() {
        when(cartService.getCartByUser(user)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(user, "123 Main St", "9876543210");

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(cartService, times(1)).clearCart(user);
    }

    @Test
    void testCreateOrderEmptyCart() {
        Cart emptyCart = new Cart(user);
        emptyCart.setCartItems(new ArrayList<>());
        when(cartService.getCartByUser(user)).thenReturn(emptyCart);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(user, "123 Main St", "9876543210"));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserOrders() {
        List<Order> orders = new ArrayList<>(List.of(order));
        when(orderRepository.findByUserOrderByOrderDateDesc(user)).thenReturn(orders);

        List<Order> result = orderService.getUserOrders(user);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserOrderByOrderDateDesc(user);
    }

    @Test
    void testGetOrdersByStatus() {
        List<Order> orders = new ArrayList<>(List.of(order));
        when(orderRepository.findByStatusOrderByOrderDateDesc(Order.OrderStatus.PENDING)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByStatus(Order.OrderStatus.PENDING);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatusOrderByOrderDateDesc(Order.OrderStatus.PENDING);
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrderStatus(1L, Order.OrderStatus.CONFIRMED);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCancelOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(notificationService, times(1)).sendOrderCancellation(user, order);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = new ArrayList<>(List.of(order));
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }
}
