package com.grupo9.auto_repair_shop.controller.reminder;

import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderRequest;
import com.grupo9.auto_repair_shop.dto.request.reminder.MaintenanceReminderUpdateRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.reminder.MaintenanceReminderResponse;
import com.grupo9.auto_repair_shop.service.reminder.MaintenanceReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MaintenanceReminderController {

    private final MaintenanceReminderService reminderService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/maintenance-reminders")
    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> create(
            @Valid @RequestBody MaintenanceReminderRequest request) {

        MaintenanceReminderResponse created = reminderService.create(request);

        ApiResponse<MaintenanceReminderResponse> response = ApiResponse.<MaintenanceReminderResponse>builder()
                .success(true)
                .message("Maintenance reminder created successfully.")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/maintenance-reminders/{id}")
    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> findById(@PathVariable UUID id) {

        MaintenanceReminderResponse found = reminderService.findById(id);

        ApiResponse<MaintenanceReminderResponse> response = ApiResponse.<MaintenanceReminderResponse>builder()
                .success(true)
                .message("Maintenance reminder retrieved successfully.")
                .data(found)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/maintenance-reminders")
    public ResponseEntity<ApiResponse<PageResponse<MaintenanceReminderResponse>>> findAll(
            @RequestParam(required = false) Boolean sent,
            @RequestParam(required = false) Boolean acknowledged,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<MaintenanceReminderResponse> result =
                reminderService.findAll(null, null, sent, acknowledged, page, size);

        ApiResponse<PageResponse<MaintenanceReminderResponse>> response =
                ApiResponse.<PageResponse<MaintenanceReminderResponse>>builder()
                        .success(true)
                        .message("Maintenance reminders retrieved successfully.")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/api/clients/{clientId}/maintenance-reminders")
    public ResponseEntity<ApiResponse<PageResponse<MaintenanceReminderResponse>>> findByClient(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<MaintenanceReminderResponse> result =
                reminderService.findAll(null, clientId, null, null, page, size);

        ApiResponse<PageResponse<MaintenanceReminderResponse>> response =
                ApiResponse.<PageResponse<MaintenanceReminderResponse>>builder()
                        .success(true)
                        .message("Maintenance reminders retrieved successfully.")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @GetMapping("/api/vehicles/{vehicleId}/maintenance-reminders")
    public ResponseEntity<ApiResponse<PageResponse<MaintenanceReminderResponse>>> findByVehicle(
            @PathVariable UUID vehicleId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<MaintenanceReminderResponse> result =
                reminderService.findAll(vehicleId, null, null, null, page, size);

        ApiResponse<PageResponse<MaintenanceReminderResponse>> response =
                ApiResponse.<PageResponse<MaintenanceReminderResponse>>builder()
                        .success(true)
                        .message("Maintenance reminders retrieved successfully.")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/maintenance-reminders/{id}")
    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MaintenanceReminderUpdateRequest request) {

        MaintenanceReminderResponse updated = reminderService.update(id, request);

        ApiResponse<MaintenanceReminderResponse> response = ApiResponse.<MaintenanceReminderResponse>builder()
                .success(true)
                .message("Maintenance reminder updated successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/maintenance-reminders/{id}/sent")
    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> markAsSent(@PathVariable UUID id) {

        MaintenanceReminderResponse updated = reminderService.markAsSent(id);

        ApiResponse<MaintenanceReminderResponse> response = ApiResponse.<MaintenanceReminderResponse>builder()
                .success(true)
                .message("Maintenance reminder marked as sent.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PatchMapping("/api/maintenance-reminders/{id}/acknowledge")
    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> acknowledge(@PathVariable UUID id) {

        MaintenanceReminderResponse updated = reminderService.acknowledge(id);

        ApiResponse<MaintenanceReminderResponse> response = ApiResponse.<MaintenanceReminderResponse>builder()
                .success(true)
                .message("Maintenance reminder acknowledged successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/maintenance-reminders/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        reminderService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Maintenance reminder deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}