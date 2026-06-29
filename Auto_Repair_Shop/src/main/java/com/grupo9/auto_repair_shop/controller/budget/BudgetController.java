package com.grupo9.auto_repair_shop.controller.budget;

import com.grupo9.auto_repair_shop.dto.request.budget.BudgetItemRequest;
import com.grupo9.auto_repair_shop.dto.request.budget.BudgetRequest;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetItemResponse;
import com.grupo9.auto_repair_shop.dto.response.budget.BudgetResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.service.budget.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/work-orders/{workOrderId}/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<BudgetResponse>> create(
            @PathVariable UUID workOrderId,
            @Valid @RequestBody BudgetRequest request
    ) {
        BudgetResponse budget = budgetService.create(workOrderId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget created successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<BudgetResponse>> findByWorkOrder(
            @PathVariable UUID workOrderId
    ) {
        BudgetResponse budget = budgetService.findByWorkOrder(workOrderId);

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget retrieved successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<BudgetResponse>> update(
            @PathVariable UUID workOrderId,
            @Valid @RequestBody BudgetRequest request
    ) {
        BudgetResponse budget = budgetService.update(workOrderId, request);

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget updated successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<BudgetResponse>> send(
            @PathVariable UUID workOrderId
    ) {
        BudgetResponse budget = budgetService.send(workOrderId);

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget sent to client successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/approve")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApiResponse<BudgetResponse>> approve(
            @PathVariable UUID workOrderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        BudgetResponse budget = budgetService.approve(workOrderId, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget approved successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/reject")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApiResponse<BudgetResponse>> reject(
            @PathVariable UUID workOrderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        BudgetResponse budget = budgetService.reject(workOrderId, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget rejected successfully")
                        .data(budget)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<BudgetItemResponse>> addItem(
            @PathVariable UUID workOrderId,
            @Valid @RequestBody BudgetItemRequest request
    ) {
        BudgetItemResponse item = budgetService.addItem(workOrderId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BudgetItemResponse>builder()
                        .success(true)
                        .message("Item added to budget successfully")
                        .data(item)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<BudgetItemResponse>> updateItem(
            @PathVariable UUID workOrderId,
            @PathVariable UUID itemId,
            @Valid @RequestBody BudgetItemRequest request
    ) {
        BudgetItemResponse item = budgetService.updateItem(workOrderId, itemId, request);

        return ResponseEntity.ok(
                ApiResponse.<BudgetItemResponse>builder()
                        .success(true)
                        .message("Item updated successfully")
                        .data(item)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable UUID workOrderId,
            @PathVariable UUID itemId
    ) {
        budgetService.deleteItem(workOrderId, itemId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Item removed from budget successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}