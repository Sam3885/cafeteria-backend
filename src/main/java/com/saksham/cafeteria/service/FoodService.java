package com.saksham.cafeteria.service;

import java.util.List;
import com.saksham.cafeteria.dto.FoodRequest;
import com.saksham.cafeteria.dto.FoodResponse;

public interface FoodService {

    FoodResponse addFood(FoodRequest request);

    List<FoodResponse> getAllFoods();

    FoodResponse getFoodById(Long id);

    FoodResponse updateFood(Long id, FoodRequest request);

    void deleteFood(Long id);
}