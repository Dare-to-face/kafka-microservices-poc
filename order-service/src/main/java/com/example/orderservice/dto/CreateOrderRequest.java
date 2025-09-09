package com.example.orderservice.dto;

import com.example.orderservice.entitiy.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private String customerId;
    private List<OrderItem> items;

}
