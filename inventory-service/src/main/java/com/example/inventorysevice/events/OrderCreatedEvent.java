package com.example.inventorysevice.events;

import com.example.inventorysevice.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
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

}