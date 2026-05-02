package com.saksham.cafeteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saksham.cafeteria.dto.AdminAnalyticsResponse;
import com.saksham.cafeteria.service.AdminAnalyticsService;

@RestController
public class AdminController {

    @Autowired
    private AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/admin/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAnalyticsResponse> getAnalytics() {

        AdminAnalyticsResponse response = adminAnalyticsService.getAnalytics();

        return ResponseEntity.ok(response);
    }
}