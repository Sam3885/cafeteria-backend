package com.saksham.cafeteria.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long foodId;
    private String foodName;
    private Integer quantity;
    private Double price;
}