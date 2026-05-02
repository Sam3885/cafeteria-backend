package com.saksham.cafeteria.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.saksham.cafeteria.dto.AdminAnalyticsResponse;
import com.saksham.cafeteria.repository.OrderItemRepository;
import com.saksham.cafeteria.repository.OrderRepository;
import com.saksham.cafeteria.service.AdminAnalyticsService;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public AdminAnalyticsResponse getAnalytics() {

        // Top 3 foods
        List<String> topFoods = orderItemRepository.findTopFoods(PageRequest.of(0, 3));

        if (topFoods.isEmpty()) {
            topFoods = List.of("No Orders Yet");
        }

        // Average order value
        Double avg = orderRepository.averageOrderValue();
        double averageOrderValue = avg != null ? avg : 0;

        // Pending orders
        Long pending = orderRepository.countPendingOrders();
        long pendingOrders = pending != null ? pending : 0;

        // Orders today
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();

        Long todayOrders = orderRepository.countOrdersBetween(startOfDay, startOfNextDay);
        long ordersToday = todayOrders != null ? todayOrders : 0;

        return new AdminAnalyticsResponse(
                topFoods,
                averageOrderValue,
                pendingOrders,
                ordersToday
        );
    }
}