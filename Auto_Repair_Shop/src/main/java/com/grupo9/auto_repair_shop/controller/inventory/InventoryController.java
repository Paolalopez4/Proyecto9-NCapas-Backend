package com.grupo9.auto_repair_shop.controller.inventory;

import com.grupo9.auto_repair_shop.dto.request.inventory.BranchInventoryRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.inventory.BranchInventoryResponse;
import com.grupo9.auto_repair_shop.service.inventory.BranchInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final BranchInventoryService branchInventoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<BranchInventoryResponse>> create(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchInventoryRequest request) {

        BranchInventoryResponse created = branchInventoryService.create(branchId, request);

        ApiResponse<BranchInventoryResponse> response = ApiResponse.<BranchInventoryResponse>builder()
                .success(true)
                .message("Inventory entry created successfully.")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{partId}")
    public ResponseEntity<ApiResponse<BranchInventoryResponse>> getByBranchAndPart(
            @PathVariable UUID branchId,
            @PathVariable UUID partId) {

        BranchInventoryResponse found = branchInventoryService.findByBranchAndPart(branchId, partId);

        ApiResponse<BranchInventoryResponse> body = ApiResponse.<BranchInventoryResponse>builder()
                .success(true)
                .message("Inventory entry retrieved successfully.")
                .data(found)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BranchInventoryResponse>>> getAll(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BranchInventoryResponse> result = branchInventoryService.findAllByBranch(branchId, page, size);

        ApiResponse<PageResponse<BranchInventoryResponse>> body = ApiResponse.<PageResponse<BranchInventoryResponse>>builder()
                .success(true)
                .message("Branch inventory retrieved successfully.")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<PageResponse<BranchInventoryResponse>>> getLowStock(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BranchInventoryResponse> result = branchInventoryService.findLowStockByBranch(branchId, page, size);

        ApiResponse<PageResponse<BranchInventoryResponse>> body = ApiResponse.<PageResponse<BranchInventoryResponse>>builder()
                .success(true)
                .message("Low stock parts retrieved successfully.")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{partId}")
    public ResponseEntity<ApiResponse<BranchInventoryResponse>> update(
            @PathVariable UUID branchId,
            @PathVariable UUID partId,
            @Valid @RequestBody BranchInventoryRequest request) {

        BranchInventoryResponse updated = branchInventoryService.update(branchId, partId, request);

        ApiResponse<BranchInventoryResponse> body = ApiResponse.<BranchInventoryResponse>builder()
                .success(true)
                .message("Inventory entry updated successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    @PatchMapping("/{partId}/consume")
    public ResponseEntity<ApiResponse<BranchInventoryResponse>> consumeStock(
            @PathVariable UUID branchId,
            @PathVariable UUID partId,
            @RequestParam Integer quantity) {

        BranchInventoryResponse updated = branchInventoryService.consumeStock(branchId, partId, quantity);

        ApiResponse<BranchInventoryResponse> body = ApiResponse.<BranchInventoryResponse>builder()
                .success(true)
                .message("Stock consumed successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }
}