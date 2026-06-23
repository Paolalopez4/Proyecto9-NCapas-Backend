package com.grupo9.auto_repair_shop.controller.service;

import com.grupo9.auto_repair_shop.dto.request.service.ServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.service.ServiceResponse;
import com.grupo9.auto_repair_shop.service.service.ServiceCatalogServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceCatalogServiceImpl serviceCatalogService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<ServiceResponse> data = serviceCatalogService.findAll(name, active, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<ServiceResponse>>builder()
                .success(true)
                .message("Services retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ServiceResponse>> findById(@PathVariable UUID id) {
        ServiceResponse data = serviceCatalogService.findById(id);
        return ResponseEntity.ok(ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceResponse>> create(
            @Valid @RequestBody ServiceRequest request) {

        ServiceResponse data = serviceCatalogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ServiceResponse>builder()
                        .success(true)
                        .message("Service created successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ServiceRequest request) {

        ServiceResponse data = serviceCatalogService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service updated successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceResponse>> toggleActive(
            @PathVariable UUID id,
            @RequestParam boolean active) {

        ServiceResponse data = serviceCatalogService.toggleActive(id, active);
        String action = active ? "activated" : "deactivated";
        return ResponseEntity.ok(ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service " + action + " successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        serviceCatalogService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Service deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}