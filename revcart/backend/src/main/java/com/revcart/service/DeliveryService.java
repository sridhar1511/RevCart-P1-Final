package com.revcart.service;

import com.revcart.entity.DeliveryAgent;
import com.revcart.entity.Order;
import com.revcart.repository.DeliveryAgentRepository;
import com.revcart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Map<Long, Map<String, Object>> agentLocations = new HashMap<>();

    public DeliveryAgent assignDeliveryAgent(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryAgent agent = deliveryAgentRepository.findAll().stream()
            .filter(a -> a.getStatus() == DeliveryAgent.Status.AVAILABLE)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No available delivery agents"));

        agent.setStatus(DeliveryAgent.Status.BUSY);
        deliveryAgentRepository.save(agent);

        return agent;
    }

    public void updateDeliveryStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        orderRepository.save(order);

        if (status == Order.OrderStatus.DELIVERED) {
            DeliveryAgent agent = deliveryAgentRepository.findAll().stream()
                .filter(a -> a.getStatus() == DeliveryAgent.Status.BUSY)
                .findFirst()
                .orElse(null);
            
            if (agent != null) {
                agent.setStatus(DeliveryAgent.Status.AVAILABLE);
                deliveryAgentRepository.save(agent);
            }
        }
    }

    public List<DeliveryAgent> getAvailableAgents() {
        return deliveryAgentRepository.findAll().stream()
            .filter(a -> a.getStatus() == DeliveryAgent.Status.AVAILABLE)
            .toList();
    }

    public DeliveryAgent getAgentById(Long agentId) {
        return deliveryAgentRepository.findById(agentId)
            .orElseThrow(() -> new RuntimeException("Agent not found"));
    }

    public void updateAgentStatus(Long agentId, DeliveryAgent.Status status) {
        DeliveryAgent agent = getAgentById(agentId);
        agent.setStatus(status);
        deliveryAgentRepository.save(agent);
    }

    public void updateAgentLocation(Double latitude, Double longitude) {
        Map<String, Object> location = new HashMap<>();
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        location.put("timestamp", System.currentTimeMillis());
        agentLocations.put(1L, location);
    }

    public Map<String, Object> getDeliveryLocation(Long orderId) {
        return agentLocations.getOrDefault(orderId, Map.of(
            "latitude", 0.0,
            "longitude", 0.0,
            "timestamp", System.currentTimeMillis()
        ));
    }

    public List<Order> getDeliveryOrders() {
        return orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != Order.OrderStatus.DELIVERED && 
                           order.getStatus() != Order.OrderStatus.CANCELLED)
            .toList();
    }
}
