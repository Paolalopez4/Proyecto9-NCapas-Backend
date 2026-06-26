package com.grupo9.auto_repair_shop.controller.vehicle;

import com.grupo9.auto_repair_shop.dto.request.vehicle.UpdateVehicleRequest;
import com.grupo9.auto_repair_shop.dto.request.vehicle.VehicleRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.vehicle.VehicleResponse;
import com.grupo9.auto_repair_shop.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> create(
            @Valid @RequestBody VehicleRequest request) {

        VehicleResponse created = vehicleService.create(request);

        ApiResponse<VehicleResponse> body = ApiResponse.<VehicleResponse>builder()
                .success(true)
                .message("Vehicle registered successfully.")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getById(@PathVariable UUID id) {

        VehicleResponse found = vehicleService.findById(id);

        ApiResponse<VehicleResponse> body = ApiResponse.<VehicleResponse>builder()
                .success(true)
                .message("Vehicle retrieved successfully.")
                .data(found)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VehicleResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<VehicleResponse> result = vehicleService.findAll(page, size);

        ApiResponse<PageResponse<VehicleResponse>> body = ApiResponse.<PageResponse<VehicleResponse>>builder()
                .success(true)
                .message("Vehicles retrieved successfully.")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<ApiResponse<PageResponse<VehicleResponse>>> getByClientId(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<VehicleResponse> result = vehicleService.findByClientId(clientId, page, size);

        ApiResponse<PageResponse<VehicleResponse>> body = ApiResponse.<PageResponse<VehicleResponse>>builder()
                .success(true)
                .message("Client's vehicles retrieved successfully.")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVehicleRequest request) {

        VehicleResponse updated = vehicleService.update(id, request);

        ApiResponse<VehicleResponse> body = ApiResponse.<VehicleResponse>builder()
                .success(true)
                .message("Vehicle updated successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        vehicleService.delete(id);

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(true)
                .message("Vehicle deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

}
