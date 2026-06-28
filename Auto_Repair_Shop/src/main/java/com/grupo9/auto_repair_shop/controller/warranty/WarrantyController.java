package com.grupo9.auto_repair_shop.controller.warranty;

import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.warranty.WarrantyResponse;
import com.grupo9.auto_repair_shop.service.warranty.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WarrantyController {

    private final WarrantyService warrantyService;

    @GetMapping("/api/warranties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<WarrantyResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<WarrantyResponse> warranties = warrantyService.findAll(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<WarrantyResponse>>builder()
                        .success(true)
                        .message("Warranties retrieved successfully")
                        .data(warranties)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/warranties/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<WarrantyResponse>> findById(
            @PathVariable UUID id
    ) {
        WarrantyResponse warranty = warrantyService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.<WarrantyResponse>builder()
                        .success(true)
                        .message("Warranty retrieved successfully")
                        .data(warranty)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/work-orders/{workOrderId}/warranty")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<WarrantyResponse>> findByWorkOrder(
            @PathVariable UUID workOrderId
    ) {
        WarrantyResponse warranty = warrantyService.findByWorkOrder(workOrderId);

        return ResponseEntity.ok(
                ApiResponse.<WarrantyResponse>builder()
                        .success(true)
                        .message("Warranty retrieved successfully")
                        .data(warranty)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/api/clients/{clientId}/warranties")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<PageResponse<WarrantyResponse>>> findByClient(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<WarrantyResponse> warranties = warrantyService.findByClient(clientId, page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<WarrantyResponse>>builder()
                        .success(true)
                        .message("Client warranties retrieved successfully")
                        .data(warranties)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/api/warranties/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WarrantyResponse>> deactivate(
            @PathVariable UUID id
    ) {
        WarrantyResponse warranty = warrantyService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.<WarrantyResponse>builder()
                        .success(true)
                        .message("Warranty deactivated successfully")
                        .data(warranty)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}