package com.example.inventorysevice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdate {
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

}
