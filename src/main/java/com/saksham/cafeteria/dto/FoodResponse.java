package com.saksham.cafeteria.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodResponse {

    private Long id;
    private String name;
    private Double price;
    private Boolean available;
    private String imageUrl;
    private String category;
}