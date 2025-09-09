// OrderEventListener.java
package com.example.inventorysevice.service;

import com.example.inventorysevice.events.InventoryUpdatedEvent;
import com.example.inventorysevice.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);


    private final InventoryService inventoryService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventListener(KafkaTemplate<String, Object> kafkaTemplate,InventoryService inventoryService) {
        this.kafkaTemplate = kafkaTemplate;
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-service-group")
    public void handleOrderCreatedEvent(
            @Payload OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        logger.info("🎯 Received OrderCreatedEvent from topic: {}, partition: {}, offset: {}",
                topic, partition, offset);
        logger.info("📦 Processing order: {} for customer: {}",
                event.getOrderId(), event.getCustomerId());

        try {
            // Process the order items
            InventoryUpdatedEvent inventoryEvent = inventoryService.processOrderItems(event);

            // Publish inventory update event
            logger.info("📤 Publishing InventoryUpdatedEvent for order: {}", event.getOrderId());
            kafkaTemplate.send("inventory-events", event.getOrderId(), inventoryEvent);

            // Acknowledge message processing
            ack.acknowledge();
            logger.info("✅ Successfully processed order: {}", event.getOrderId());

        } catch (Exception e) {
            logger.error("❌ Error processing order event: {}", event.getOrderId(), e);
            // Don't acknowledge - message will be retried
        }
    }
}
