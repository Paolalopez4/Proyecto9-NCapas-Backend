package com.grupo9.auto_repair_shop.controller.workorder;

import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderPartRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderRequest;
import com.grupo9.auto_repair_shop.dto.request.workorder.WorkOrderServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.workorder.WorkOrderResponse;
import com.grupo9.auto_repair_shop.enums.OrderType;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import com.grupo9.auto_repair_shop.service.workorder.WorkOrderService;
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
@RequiredArgsConstructor
public class WorkOrderController {
    private final WorkOrderService workOrderService;

    @GetMapping("/api/work-orders")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<Page<WorkOrderResponse>>> findAll(
            @RequestParam(required = false) WorkOrderStatus status,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID mechanicId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<WorkOrderResponse> data = workOrderService.findAll(
                status, orderType, branchId, mechanicId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<WorkOrderResponse>>builder()
                .success(true)
                .message("Work orders retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/work-orders/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC','CLIENT')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> findById(@PathVariable UUID id) {
        WorkOrderResponse data = workOrderService.findById(id);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/clients/{clientId}/work-orders")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<Page<WorkOrderResponse>>> findByClientId(
            @PathVariable UUID clientId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<WorkOrderResponse> data = workOrderService.findByClientId(clientId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<WorkOrderResponse>>builder()
                .success(true)
                .message("Work orders retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/api/work-orders")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> create(
            @Valid @RequestBody WorkOrderRequest request) {

        WorkOrderResponse data = workOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<WorkOrderResponse>builder()
                        .success(true)
                        .message("Work order created successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/api/work-orders/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody WorkOrderRequest request) {

        WorkOrderResponse data = workOrderService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order updated successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/work-orders/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> start(@PathVariable UUID id) {
        WorkOrderResponse data = workOrderService.start(id);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order started successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/work-orders/{id}/request-approval")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> requestApproval(@PathVariable UUID id) {
        WorkOrderResponse data = workOrderService.requestApproval(id);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order sent for approval successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/work-orders/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> close(@PathVariable UUID id) {
        WorkOrderResponse data = workOrderService.close(id);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order closed successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/work-orders/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> cancel(@PathVariable UUID id) {
        WorkOrderResponse data = workOrderService.cancel(id);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Work order cancelled successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/api/work-orders/{id}/services")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> addService(
            @PathVariable UUID id,
            @Valid @RequestBody WorkOrderServiceRequest request) {

        WorkOrderResponse data = workOrderService.addService(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<WorkOrderResponse>builder()
                        .success(true)
                        .message("Service added to work order successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/api/work-orders/{id}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> updateService(
            @PathVariable UUID id,
            @PathVariable UUID serviceId,
            @Valid @RequestBody WorkOrderServiceRequest request) {

        WorkOrderResponse data = workOrderService.updateService(id, serviceId, request);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Service updated in work order successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/api/work-orders/{id}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> removeService(
            @PathVariable UUID id,
            @PathVariable UUID serviceId) {

        WorkOrderResponse data = workOrderService.removeService(id, serviceId);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Service removed from work order successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/api/work-orders/{id}/parts")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> addPart(
            @PathVariable UUID id,
            @Valid @RequestBody WorkOrderPartRequest request) {

        WorkOrderResponse data = workOrderService.addPart(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<WorkOrderResponse>builder()
                        .success(true)
                        .message("Part added to work order successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/api/work-orders/{id}/parts/{partId}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> updatePart(
            @PathVariable UUID id,
            @PathVariable UUID partId,
            @Valid @RequestBody WorkOrderPartRequest request) {

        WorkOrderResponse data = workOrderService.updatePart(id, partId, request);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Part updated in work order successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/api/work-orders/{id}/parts/{partId}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> removePart(
            @PathVariable UUID id,
            @PathVariable UUID partId) {

        WorkOrderResponse data = workOrderService.removePart(id, partId);
        return ResponseEntity.ok(ApiResponse.<WorkOrderResponse>builder()
                .success(true)
                .message("Part removed from work order successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }
}