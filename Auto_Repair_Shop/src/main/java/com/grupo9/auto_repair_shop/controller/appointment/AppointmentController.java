package com.grupo9.auto_repair_shop.controller.appointment;

import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentRequest;
import com.grupo9.auto_repair_shop.dto.request.appointment.AppointmentServiceRequest;
import com.grupo9.auto_repair_shop.dto.response.appointment.AppointmentResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.service.appointment.AppointmentService;
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
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/api/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> findAll(
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID mechanicId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<AppointmentResponse> data = appointmentService.findAll(
                branchId, date, status, mechanicId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<AppointmentResponse>>builder()
                .success(true)
                .message("Appointments retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/appointments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC','CLIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> findById(@PathVariable UUID id) {
        AppointmentResponse data = appointmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.<AppointmentResponse>builder()
                .success(true)
                .message("Appointment retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/clients/{clientId}/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> findByClientId(
            @PathVariable UUID clientId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<AppointmentResponse> data = appointmentService.findByClientId(clientId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<AppointmentResponse>>builder()
                .success(true)
                .message("Appointments retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/api/appointments/available-slots")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LocalDateTime>>> getAvailableSlots(
            @RequestParam UUID branchId,
            @RequestParam LocalDateTime date) {

        List<LocalDateTime> data = appointmentService.getAvailableSlots(branchId, date);
        return ResponseEntity.ok(ApiResponse.<List<LocalDateTime>>builder()
                .success(true)
                .message("Available slots retrieved successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/api/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse data = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AppointmentResponse>builder()
                        .success(true)
                        .message("Appointment booked successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/api/appointments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse data = appointmentService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<AppointmentResponse>builder()
                .success(true)
                .message("Appointment updated successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/appointments/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN','MECHANIC')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirm(@PathVariable UUID id) {
        AppointmentResponse data = appointmentService.confirm(id);
        return ResponseEntity.ok(ApiResponse.<AppointmentResponse>builder()
                .success(true)
                .message("Appointment confirmed successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/api/appointments/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancel(@PathVariable UUID id) {
        AppointmentResponse data = appointmentService.cancel(id);
        return ResponseEntity.ok(ApiResponse.<AppointmentResponse>builder()
                .success(true)
                .message("Appointment cancelled successfully.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/api/appointments/{id}/services")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> addService(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentServiceRequest request) {

        AppointmentResponse data = appointmentService.addService(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AppointmentResponse>builder()
                        .success(true)
                        .message("Service added to appointment successfully.")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @DeleteMapping("/api/appointments/{id}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<ApiResponse<Void>> removeService(
            @PathVariable UUID id,
            @PathVariable UUID serviceId) {

        appointmentService.removeService(id, serviceId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Service removed from appointment successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }
}