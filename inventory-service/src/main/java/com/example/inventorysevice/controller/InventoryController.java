package com.example.inventorysevice.controller;


import com.example.inventorysevice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Integer>> getCurrentInventory() {
        return ResponseEntity.ok(inventoryService.getCurrentInventory());
    }
}
