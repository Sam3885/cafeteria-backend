package com.saksham.cafeteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saksham.cafeteria.entity.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

}