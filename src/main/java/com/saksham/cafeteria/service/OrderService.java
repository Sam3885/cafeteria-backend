package com.saksham.cafeteria.service;

import com.saksham.cafeteria.dto.OrderRequest;
import com.saksham.cafeteria.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    List<OrderResponse> getAllOrders();

    void deleteOrder(Long id);
    OrderResponse updateOrderStatus(Long id, String status);
}