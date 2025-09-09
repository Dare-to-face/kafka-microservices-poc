package com.example.inventorysevice.events;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class InventoryEvent {
    private String orderId;
    private LocalDateTime timestamp;
    private String eventType;

    protected InventoryEvent() {
        this.timestamp = LocalDateTime.now();
    }

    protected InventoryEvent(String orderId, String eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

}