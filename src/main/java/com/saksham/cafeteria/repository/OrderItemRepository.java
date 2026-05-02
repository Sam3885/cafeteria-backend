package com.saksham.cafeteria.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saksham.cafeteria.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT oi.food.name
        FROM OrderItem oi
        GROUP BY oi.food.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<String> findTopFoods(Pageable pageable);
}