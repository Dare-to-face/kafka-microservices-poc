package com.example.inventorysevice.events;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryUpdatedEvent extends InventoryEvent {
    private List<InventoryUpdate> updates;
    private String status; // SUCCESS or FAILED
    private String message;

    public InventoryUpdatedEvent() {
        super();
    }

    public InventoryUpdatedEvent(String orderId, List<InventoryUpdate> updates, String status, String message) {
        super(orderId, "INVENTORY_UPDATED");
        this.updates = updates;
        this.status = status;
        this.message = message;
    }



    // Inner class
    public static class InventoryUpdate {
        private String productId;
        private int quantityReserved;
        private int remainingStock;

        public InventoryUpdate() {
        }

        public InventoryUpdate(String productId, int quantityReserved, int remainingStock) {
            this.productId = productId;
            this.quantityReserved = quantityReserved;
            this.remainingStock = remainingStock;
        }

        // Getters and setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantityReserved() {
            return quantityReserved;
        }

        public void setQuantityReserved(int quantityReserved) {
            this.quantityReserved = quantityReserved;
        }

        public int getRemainingStock() {
            return remainingStock;
        }

        public void setRemainingStock(int remainingStock) {
            this.remainingStock = remainingStock;
        }
    }
}
