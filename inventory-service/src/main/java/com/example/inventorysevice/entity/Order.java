package com.example.inventorysevice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID orderId;
    private LocalDateTime orderDate;
    private String userName;
    private String orderStatus;
    private List<OrderItem> orderItems;
}

