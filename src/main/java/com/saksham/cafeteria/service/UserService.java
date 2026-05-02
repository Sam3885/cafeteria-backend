package com.saksham.cafeteria.service;

import com.saksham.cafeteria.dto.*;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    AuthResponse login(LoginRequest request);
}