package com.grupo9.auto_repair_shop.service.auth;

import com.grupo9.auto_repair_shop.dto.request.auth.ChangePasswordRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.LoginRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.RegisterRequest;
import com.grupo9.auto_repair_shop.dto.response.auth.AuthUserResponse;
import com.grupo9.auto_repair_shop.dto.response.auth.LoginResponse;

public interface AuthService {

    AuthUserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void logout();

    AuthUserResponse changePassword(ChangePasswordRequest request);
}