package com.grupo9.auto_repair_shop.controller.mechanic;

import com.grupo9.auto_repair_shop.dto.request.mechanic.MechanicRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.mechanic.MechanicResponse;
import com.grupo9.auto_repair_shop.service.mechanic.MechanicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mechanics")
public class MechanicController {

        private final MechanicService mechanicService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<ApiResponse<MechanicResponse>> create(
                @Valid @RequestBody MechanicRequest request) {

            MechanicResponse created = mechanicService.create(request);

            ApiResponse<MechanicResponse> body = ApiResponse.<MechanicResponse>builder()
                    .success(true)
                    .message("Mechanic created successfully.")
                    .data(created)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<MechanicResponse>> getById(@PathVariable UUID id) {

            MechanicResponse found = mechanicService.findById(id);

            ApiResponse<MechanicResponse> body = ApiResponse.<MechanicResponse>builder()
                    .success(true)
                    .message("Mechanic retrieved successfully.")
                    .data(found)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(body);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping
        public ResponseEntity<ApiResponse<PageResponse<MechanicResponse>>> getAll(
                @RequestParam(required = false) UUID branchId,
                @RequestParam(required = false) String specialty,
                @RequestParam(required = false) Boolean active,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

            PageResponse<MechanicResponse> result =
                    mechanicService.findAll(branchId, specialty, active, page, size);

            ApiResponse<PageResponse<MechanicResponse>> body = ApiResponse.<PageResponse<MechanicResponse>>builder()
                    .success(true)
                    .message("Mechanics retrieved successfully.")
                    .data(result)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(body);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<MechanicResponse>> update(
                @PathVariable UUID id,
                @Valid @RequestBody MechanicRequest request) {

            MechanicResponse updated = mechanicService.update(id, request);

            ApiResponse<MechanicResponse> body = ApiResponse.<MechanicResponse>builder()
                    .success(true)
                    .message("Mechanic updated successfully.")
                    .data(updated)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(body);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PatchMapping("/{id}/active")
        public ResponseEntity<ApiResponse<MechanicResponse>> updateActive(
                @PathVariable UUID id,
                @RequestBody Map<String, Boolean> requestBody) {

            boolean active = requestBody.get("active");
            MechanicResponse updated = mechanicService.updateActive(id, active);

            ApiResponse<MechanicResponse> body = ApiResponse.<MechanicResponse>builder()
                    .success(true)
                    .message(active ? "Mechanic activated successfully." : "Mechanic deactivated successfully.")
                    .data(updated)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(body);
        }
}
