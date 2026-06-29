package com.grupo9.auto_repair_shop.controller.repairhistory;

import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.repairhistory.RepairHistoryResponse;
import com.grupo9.auto_repair_shop.service.repairhistory.RepairHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RepairHistoryController {
    private final RepairHistoryService repairHistoryService;

    @GetMapping("/api/repair-history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<RepairHistoryResponse>>> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<RepairHistoryResponse> data = repairHistoryService.findAll(from, to, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<RepairHistoryResponse>>builder()
                .success(true)
                .message("Repair history retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/repair-history/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC','CLIENT')")
    public ResponseEntity<ApiResponse<RepairHistoryResponse>> findById(@PathVariable UUID id) {
        RepairHistoryResponse data = repairHistoryService.findById(id);
        return ResponseEntity.ok(ApiResponse.<RepairHistoryResponse>builder()
                .success(true)
                .message("Repair history retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/vehicles/{vehicleId}/repair-history")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC','CLIENT')")
    public ResponseEntity<ApiResponse<List<RepairHistoryResponse>>> findByVehicleId(
            @PathVariable UUID vehicleId) {

        List<RepairHistoryResponse> data = repairHistoryService.findByVehicleId(vehicleId);
        return ResponseEntity.ok(ApiResponse.<List<RepairHistoryResponse>>builder()
                .success(true)
                .message("Repair history retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }
}