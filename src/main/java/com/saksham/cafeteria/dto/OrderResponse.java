package com.saksham.cafeteria.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private Double totalAmount;

    private LocalDateTime createdAt;

    private String status;   // <-- Newly added
    private String orderedBy;

    private List<OrderItemResponse> items;
}