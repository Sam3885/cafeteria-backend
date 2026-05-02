package com.saksham.cafeteria.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String name;
    private String email;
    private String password;
}