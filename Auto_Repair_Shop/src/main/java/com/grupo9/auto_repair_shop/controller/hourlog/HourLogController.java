package com.grupo9.auto_repair_shop.controller.hourlog;

import com.grupo9.auto_repair_shop.dto.request.hourlog.HourLogRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.hourlog.HourLogResponse;
import com.grupo9.auto_repair_shop.service.hourlog.HourLogService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class HourLogController {

    private final HourLogService hourLogService;

    @PostMapping("/work-orders/{workOrderId}/hour-logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<HourLogResponse>> create(
            @PathVariable UUID workOrderId,
            @Valid @RequestBody HourLogRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        HourLogResponse hourLog = hourLogService.create(
                workOrderId, request, userDetails.getUsername()
        );

        ApiResponse<HourLogResponse> response = ApiResponse.<HourLogResponse>builder()
                .success(true)
                .message("Hour log created successfully")
                .data(hourLog)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/work-orders/{workOrderId}/hour-logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<PageResponse<HourLogResponse>>> findByWorkOrder(
            @PathVariable UUID workOrderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<HourLogResponse> hourLogs = hourLogService.findByWorkOrder(workOrderId, page, size);

        ApiResponse<PageResponse<HourLogResponse>> response = ApiResponse.<PageResponse<HourLogResponse>>builder()
                .success(true)
                .message("Hour logs retrieved successfully")
                .data(hourLogs)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/work-orders/{workOrderId}/hour-logs/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<HourLogResponse>> findById(
            @PathVariable UUID workOrderId,
            @PathVariable UUID id
    ) {
        HourLogResponse hourLog = hourLogService.findById(workOrderId, id);

        ApiResponse<HourLogResponse> response = ApiResponse.<HourLogResponse>builder()
                .success(true)
                .message("Hour log retrieved successfully")
                .data(hourLog)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mechanics/{mechanicId}/hour-logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<ApiResponse<PageResponse<HourLogResponse>>> findByMechanic(
            @PathVariable UUID mechanicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<HourLogResponse> hourLogs = hourLogService.findByMechanic(mechanicId, page, size);

        ApiResponse<PageResponse<HourLogResponse>> response = ApiResponse.<PageResponse<HourLogResponse>>builder()
                .success(true)
                .message("Mechanic hour logs retrieved successfully")
                .data(hourLogs)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/work-orders/{workOrderId}/hour-logs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HourLogResponse>> update(
            @PathVariable UUID workOrderId,
            @PathVariable UUID id,
            @Valid @RequestBody HourLogRequest request
    ) {
        HourLogResponse hourLog = hourLogService.update(workOrderId, id, request);

        ApiResponse<HourLogResponse> response = ApiResponse.<HourLogResponse>builder()
                .success(true)
                .message("Hour log updated successfully")
                .data(hourLog)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/work-orders/{workOrderId}/hour-logs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID workOrderId,
            @PathVariable UUID id
    ) {
        hourLogService.delete(workOrderId, id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Hour log deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}