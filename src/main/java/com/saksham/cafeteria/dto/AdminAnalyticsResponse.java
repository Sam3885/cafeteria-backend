package com.saksham.cafeteria.dto;

import java.util.List;

public class AdminAnalyticsResponse {

    private List<String> topFoods;
    private double averageOrderValue;
    private long pendingOrders;
    private long ordersToday;

    public AdminAnalyticsResponse(List<String> topFoods,
                                  double averageOrderValue,
                                  long pendingOrders,
                                  long ordersToday) {
        this.topFoods = topFoods;
        this.averageOrderValue = averageOrderValue;
        this.pendingOrders = pendingOrders;
        this.ordersToday = ordersToday;
    }

    public List<String> getTopFoods() {
        return topFoods;
    }

    public double getAverageOrderValue() {
        return averageOrderValue;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public long getOrdersToday() {
        return ordersToday;
    }
}