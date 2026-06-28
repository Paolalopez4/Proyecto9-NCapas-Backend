package com.grupo9.auto_repair_shop.controller.auth;

import com.grupo9.auto_repair_shop.dto.request.auth.ChangePasswordRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.LoginRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.RefreshTokenRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.RegisterRequest;
import com.grupo9.auto_repair_shop.dto.response.auth.AuthUserResponse;
import com.grupo9.auto_repair_shop.dto.response.auth.LoginResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthUserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthUserResponse user = authService.register(request);

        ApiResponse<AuthUserResponse> response = ApiResponse.<AuthUserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse loginResponse = authService.login(request);

        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(loginResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logout successfully.")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<AuthUserResponse>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        AuthUserResponse user = authService.changePassword(request);

        ApiResponse<AuthUserResponse> response = ApiResponse.<AuthUserResponse>builder()
                .success(true)
                .message("Password updated successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LoginResponse response = authService.refresh(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Token refreshed successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}