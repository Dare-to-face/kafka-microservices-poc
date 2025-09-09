package com.example.orderservice.controller;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.entitiy.OrderItem;
import com.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody CreateOrderRequest request) {
        String orderId = orderService.createOrder(request.getCustomerId(), request.getItems());
        return ResponseEntity.ok(Map.of("orderId", orderId, "status", "Order created successfully"));
    }

    // Convenience endpoint for quick testing
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> createTestOrder() {
        List<OrderItem> testItems = Arrays.asList(
                new OrderItem("PRODUCT-1", "Laptop", 1, new BigDecimal("999.99")),
                new OrderItem("PRODUCT-2", "Mouse", 2, new BigDecimal("29.99"))
        );

        String orderId = orderService.createOrder("CUSTOMER-123", testItems);
        return ResponseEntity.ok(Map.of("orderId", orderId, "status", "Test order created successfully"));
    }

}
