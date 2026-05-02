package com.saksham.cafeteria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.saksham.cafeteria.dto.*;
import com.saksham.cafeteria.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return service.placeOrder(request);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return service.getAllOrders();
    }
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return "Order deleted successfully";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        return service.updateOrderStatus(id, status);
    }
}