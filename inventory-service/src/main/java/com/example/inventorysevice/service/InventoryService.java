package com.example.inventorysevice.service;

import com.example.inventorysevice.entity.OrderItem;
import com.example.inventorysevice.events.InventoryUpdatedEvent;
import com.example.inventorysevice.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    // Simple in-memory inventory (in real app, this would be database)
    private final Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        // Initialize some test inventory
        inventory.put("PRODUCT-1", 50);
        inventory.put("PRODUCT-2", 100);
        inventory.put("PRODUCT-3", 25);
        logger.info("Initialized inventory: {}", inventory);
    }

    public InventoryUpdatedEvent processOrderItems(OrderCreatedEvent orderEvent) {
        logger.info("Processing inventory for order: {}", orderEvent.getOrderId());

        List<InventoryUpdatedEvent.InventoryUpdate> updates = new ArrayList<>();
        boolean allAvailable = true;
        String message = "All items reserved successfully";

        // Check and reserve each item
        for (OrderItem item : orderEvent.getItems()) {
            String productId = item.getItemCode();
            int requestedQuantity = item.getQuantity();
            int currentStock = inventory.getOrDefault(productId, 0);

            logger.info("Processing item: {} - Requested: {}, Available: {}",
                    productId, requestedQuantity, currentStock);

            if (currentStock >= requestedQuantity) {
                // Reserve items
                int newStock = currentStock - requestedQuantity;
                inventory.put(productId, newStock);

                updates.add(new InventoryUpdatedEvent.InventoryUpdate(
                        productId, requestedQuantity, newStock));

                logger.info("Reserved {} units of {} - Remaining stock: {}",
                        requestedQuantity, productId, newStock);
            } else {
                allAvailable = false;
                message = "Insufficient stock for product: " + productId;

                updates.add(new InventoryUpdatedEvent.InventoryUpdate(
                        productId, 0, currentStock));

                logger.warn("Insufficient stock for {}: requested={}, available={}",
                        productId, requestedQuantity, currentStock);
                break;
            }
        }

        String status = allAvailable ? "SUCCESS" : "FAILED";
        logger.info("Inventory processing complete for order: {} - Status: {}",
                orderEvent.getOrderId(), status);

        return new InventoryUpdatedEvent(orderEvent.getOrderId(), updates, status, message);
    }

    public Map<String, Integer> getCurrentInventory() {
        return new HashMap<>(inventory);
    }
}
