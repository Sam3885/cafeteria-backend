package com.saksham.cafeteria.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saksham.cafeteria.entity.Order;
import com.saksham.cafeteria.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Existing method
    List<Order> findByUser(User user);

    // Pending orders
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status != 'COMPLETED'")
    Long countPendingOrders();

    // Average completed order value
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED'")
    Double averageOrderValue();

    // Orders in a given time range (used for today's orders)
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.createdAt >= :start
        AND o.createdAt < :end
    """)
    Long countOrdersBetween(LocalDateTime start, LocalDateTime end);
}