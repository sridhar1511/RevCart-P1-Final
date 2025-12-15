package com.revcart.service;

import com.revcart.entity.*;
import com.revcart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private NotificationService notificationService;

    public Order createOrder(User user, String deliveryAddress, String phoneNumber) {
        Cart cart = cartService.getCartByUser(user);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        java.util.List<CartItem> cartItems = cart.getCartItems();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem item : cartItems) {
            if (item.getProduct() != null) {
                totalAmount = totalAmount.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        Order order = new Order(user, totalAmount, deliveryAddress, phoneNumber);
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct() != null) {
                OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice());
                orderItemRepository.save(orderItem);
            }
        }

        cartService.clearCart(user);
        notificationService.sendOrderConfirmation(user, order);
        return order;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        return orderRepository.findById(orderId).map(order -> {
            order.setStatus(status);
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void cancelOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            if (order.getStatus() != Order.OrderStatus.DELIVERED && order.getStatus() != Order.OrderStatus.CANCELLED) {
                order.setStatus(Order.OrderStatus.CANCELLED);
                orderRepository.save(order);
                notificationService.sendOrderCancellation(order.getUser(), order);
            }
        });
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrderWithItems(User user, String deliveryAddress, String phoneNumber, List<Map<String, Object>> items) {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("No items provided");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (Map<String, Object> item : items) {
            Number price = (Number) item.get("price");
            Number quantity = (Number) item.get("quantity");
            if (price != null && quantity != null) {
                totalAmount = totalAmount.add(BigDecimal.valueOf(price.doubleValue() * quantity.intValue()));
            }
        }

        Order order = new Order(user, totalAmount, deliveryAddress, phoneNumber);
        order = orderRepository.save(order);

        for (Map<String, Object> item : items) {
            Number productId = (Number) item.get("id");
            Number quantity = (Number) item.get("quantity");
            Number price = (Number) item.get("price");
            
            if (productId != null && quantity != null && price != null) {
                // Create order item without needing the full product entity
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setQuantity(quantity.intValue());
                orderItem.setPrice(BigDecimal.valueOf(price.doubleValue()));
                orderItemRepository.save(orderItem);
            }
        }

        notificationService.sendOrderConfirmation(user, order);
        return order;
    }
}
