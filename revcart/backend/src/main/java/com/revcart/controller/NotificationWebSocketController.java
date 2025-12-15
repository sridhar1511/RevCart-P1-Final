package com.revcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public Map<String, Object> sendNotification(Map<String, Object> message) {
        return message;
    }

    public void sendOrderUpdate(Long userId, String status, Long orderId) {
        messagingTemplate.convertAndSend("/topic/orders/" + userId, Map.of(
            "orderId", orderId,
            "status", status,
            "timestamp", System.currentTimeMillis()
        ));
    }

    public void sendDeliveryUpdate(Long userId, String status, Double latitude, Double longitude) {
        messagingTemplate.convertAndSend("/topic/delivery/" + userId, Map.of(
            "status", status,
            "latitude", latitude,
            "longitude", longitude,
            "timestamp", System.currentTimeMillis()
        ));
    }
}
