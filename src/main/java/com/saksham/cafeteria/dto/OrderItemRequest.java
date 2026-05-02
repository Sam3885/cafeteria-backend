package com.saksham.cafeteria.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long foodId;
    private Integer quantity;
}