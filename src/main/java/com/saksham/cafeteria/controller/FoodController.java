package com.saksham.cafeteria.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import com.saksham.cafeteria.dto.*;
import com.saksham.cafeteria.service.FoodService;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService service;

    @PostMapping
    public FoodResponse addFood(@Valid @RequestBody FoodRequest request) {
        return service.addFood(request);
    }

    @GetMapping
    public List<FoodResponse> getAllFoods() {
        return service.getAllFoods();
    }

    @GetMapping("/{id}")
    public FoodResponse getFoodById(@PathVariable Long id) {
        return service.getFoodById(id);
    }
    @PutMapping("/{id}")
    public FoodResponse updateFood(@PathVariable Long id,
                                   @Valid @RequestBody FoodRequest request) {
        return service.updateFood(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteFood(@PathVariable Long id) {
        service.deleteFood(id);
        return "Food deleted successfully";
    }
}