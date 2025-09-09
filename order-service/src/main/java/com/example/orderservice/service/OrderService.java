package com.example.orderservice.service;


import com.example.orderservice.entitiy.OrderItem;
import com.example.orderservice.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String createOrder(String customerId, List<OrderItem> items) {
        // Generate order ID
        String orderId = "ORDER-" + UUID.randomUUID().toString().substring(0, 8);

        // Calculate total
        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("Creating order: {} for customer: {} with total: {}", orderId, customerId, total);

        // In real app, save to database here
        // orderRepository.save(order);

        // Create and publish event
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, items, total);

        logger.info("Publishing OrderCreatedEvent for order: {}", orderId);
        kafkaTemplate.send("order-events", orderId, event);

        return orderId;
    }
}
