package com.saksham.cafeteria.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.saksham.cafeteria.dto.*;
import com.saksham.cafeteria.entity.*;
import com.saksham.cafeteria.repository.*;
import com.saksham.cafeteria.service.OrderService;
import com.saksham.cafeteria.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final FoodItemRepository foodRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        // 🔐 Get logged-in user email from JWT
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setUser(user);   // 🔥 IMPORTANT LINE

        List<OrderItem> orderItems = request.getItems().stream().map(itemReq -> {

            FoodItem food = foodRepository.findById(itemReq.getFoodId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Food not found with id: " + itemReq.getFoodId()));

//             🔥 NEW VALIDATION
            if (!Boolean.TRUE.equals(food.getAvailable())) {
                throw new IllegalArgumentException(
                    "Food item '" + food.getName() + "' is currently not available");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(food);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(food.getPrice());
            orderItem.setOrder(order);

            return orderItem;

        }).collect(Collectors.toList());

        double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // 🔥 10% discount for orders above ₹500
        if (total > 500) {
            total = total * 0.9;
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        return buildOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Order> orders;

        // 👇 If ADMIN → see all
        if (user.getRole().name().equals("ADMIN")) {
            orders = orderRepository.findAll();
        } else {
            // 👇 If CUSTOMER → see only their orders
            orders = orderRepository.findByUser(user);
        }

        return orders.stream()
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id));

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 🔐 CUSTOMER logic
        if (user.getRole().name().equals("CUSTOMER")) {

            // Must own the order
            if (!order.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException(
                        "You cannot delete someone else's order");
            }

            // 🔥 5-minute cancellation window
            Duration duration = Duration.between(
                    order.getCreatedAt(),
                    LocalDateTime.now()
            );

            if (duration.toMinutes() > 5) {
                throw new IllegalStateException(
                        "Order can only be cancelled within 5 minutes of placing");
            }
        }

        // 👑 ADMIN can delete anytime (no time restriction)

        orderRepository.delete(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id));

        OrderStatus currentStatus = order.getStatus();

        // 🔥 EXPLICIT FINAL STATE PROTECTION
        if (currentStatus == OrderStatus.COMPLETED ||
            currentStatus == OrderStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Order is already " + currentStatus +
                    " and cannot be modified");
        }

        OrderStatus newStatus;

        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status value");
        }

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        order.setStatus(newStatus);

        Order updated = orderRepository.save(order);

        return buildOrderResponse(updated);
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {

        return switch (current) {
            case PLACED -> next == OrderStatus.PREPARING;
            case PREPARING -> next == OrderStatus.READY;
            case READY -> next == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    private OrderResponse buildOrderResponse(Order order) {

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .foodId(item.getFood().getId())
                        .foodName(item.getFood().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())
                .orderedBy(order.getUser().getEmail())   // 🔥 NEW LINE
                .items(itemResponses)
                .build();
    }
}