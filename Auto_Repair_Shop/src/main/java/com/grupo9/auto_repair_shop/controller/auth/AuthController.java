package com.grupo9.auto_repair_shop.controller.auth;

import com.grupo9.auto_repair_shop.dto.request.auth.ChangePasswordRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.LoginRequest;
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
                .message("Usuario registrado correctamente")
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
                .message("Inicio de sesión correcto")
                .data(loginResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Sesión cerrada correctamente")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<AuthUserResponse>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        AuthUserResponse user = authService.changePassword(request);

        ApiResponse<AuthUserResponse> response = ApiResponse.<AuthUserResponse>builder()
                .success(true)
                .message("Contraseña actualizada correctamente")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}