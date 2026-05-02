package com.saksham.cafeteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FoodRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    private Boolean available;
    private String imageUrl;
    private String category;
}