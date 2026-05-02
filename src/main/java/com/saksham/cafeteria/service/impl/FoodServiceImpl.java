package com.saksham.cafeteria.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.saksham.cafeteria.entity.FoodItem;
import com.saksham.cafeteria.repository.FoodItemRepository;
import com.saksham.cafeteria.dto.*;
import com.saksham.cafeteria.service.FoodService;
import com.saksham.cafeteria.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodItemRepository repository;

    @Override
    public FoodResponse addFood(FoodRequest request) {

        FoodItem food = FoodItem.builder()
                .name(request.getName())
                .price(request.getPrice())
                .available(request.getAvailable())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .build();

        FoodItem saved = repository.save(food);

        return FoodResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .available(saved.getAvailable())
                .imageUrl(saved.getImageUrl())
                .category(saved.getCategory())
                .build();
    }

    @Override
    public List<FoodResponse> getAllFoods() {

        return repository.findAll()
                .stream()
                .map(food -> FoodResponse.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .price(food.getPrice())
                        .available(food.getAvailable())
                        .imageUrl(food.getImageUrl())
                        .category(food.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFoodById(Long id) {

        FoodItem food = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Food not found with id: " + id));

        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .price(food.getPrice())
                .available(food.getAvailable())
                .imageUrl(food.getImageUrl())
                .category(food.getCategory())
                .build();
    }

    @Override
    public FoodResponse updateFood(Long id, FoodRequest request) {

        FoodItem food = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Food not found with id: " + id));

        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setAvailable(request.getAvailable());
        food.setImageUrl(request.getImageUrl());
        food.setCategory(request.getCategory());

        FoodItem updated = repository.save(food);

        return FoodResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .price(updated.getPrice())
                .available(updated.getAvailable())
                .imageUrl(updated.getImageUrl())
                .category(updated.getCategory())
                .build();
    }

    @Override
    public void deleteFood(Long id) {

        FoodItem food = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Food not found with id: " + id));

        repository.delete(food);
    }
}