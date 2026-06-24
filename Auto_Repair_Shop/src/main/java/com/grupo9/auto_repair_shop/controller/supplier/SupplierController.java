package com.grupo9.auto_repair_shop.controller.supplier;

import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierPartRequest;
import com.grupo9.auto_repair_shop.dto.request.supplier.SupplierRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierPartResponse;
import com.grupo9.auto_repair_shop.dto.response.supplier.SupplierResponse;
import com.grupo9.auto_repair_shop.service.supplier.SupplierService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<SupplierResponse>>> findAll(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<SupplierResponse> data = supplierService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<SupplierResponse>>builder()
                .success(true)
                .message("Suppliers retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierResponse>> findById(@PathVariable UUID id) {
        SupplierResponse data = supplierService.findById(id);
        return ResponseEntity.ok(ApiResponse.<SupplierResponse>builder()
                .success(true)
                .message("Supplier retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}/parts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SupplierPartResponse>>> findPartsBySupplier(
            @PathVariable UUID id) {

        List<SupplierPartResponse> data = supplierService.findPartsBySupplier(id);
        return ResponseEntity.ok(ApiResponse.<List<SupplierPartResponse>>builder()
                .success(true)
                .message("Supplier parts retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{partId}/best-supplier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierPartResponse>> getBestSupplier(
            @PathVariable UUID partId) {

        SupplierPartResponse data = supplierService.getBestSupplierForPart(partId);
        return ResponseEntity.ok(ApiResponse.<SupplierPartResponse>builder()
                .success(true)
                .message("Best supplier retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierResponse>> create(
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse data = supplierService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SupplierResponse>builder()
                        .success(true)
                        .message("Supplier created successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/{id}/parts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierPartResponse>> linkPart(
            @PathVariable UUID id,
            @Valid @RequestBody SupplierPartRequest request) {

        SupplierPartResponse data = supplierService.linkPart(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SupplierPartResponse>builder()
                        .success(true)
                        .message("Part linked to supplier successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse data = supplierService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<SupplierResponse>builder()
                .success(true)
                .message("Supplier updated successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}/parts/{partId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierPartResponse>> updateSupplierPart(
            @PathVariable UUID id,
            @PathVariable UUID partId,
            @Valid @RequestBody SupplierPartRequest request) {

        SupplierPartResponse data = supplierService.updateSupplierPart(id, partId, request);
        return ResponseEntity.ok(ApiResponse.<SupplierPartResponse>builder()
                .success(true)
                .message("Supplier part updated successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        supplierService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Supplier deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}/parts/{partId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeSupplierPart(
            @PathVariable UUID id,
            @PathVariable UUID partId) {

        supplierService.removeSupplierPart(id, partId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Part removed from supplier successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}