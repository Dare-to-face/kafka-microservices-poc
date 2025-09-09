package com.example.orderservice.events;

import com.example.orderservice.entitiy.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEvent extends OrderEvent {
    private String customerId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;

    public OrderCreatedEvent() {
        super();
    }

    public OrderCreatedEvent(String orderId, String customerId, List<OrderItem> items, BigDecimal totalAmount) {
        super(orderId, "ORDER_CREATED");
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

}