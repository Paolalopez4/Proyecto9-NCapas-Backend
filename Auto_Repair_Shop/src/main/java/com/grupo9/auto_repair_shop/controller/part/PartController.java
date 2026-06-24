package com.grupo9.auto_repair_shop.controller.part;

import com.grupo9.auto_repair_shop.dto.request.part.PartRequest;
import com.grupo9.auto_repair_shop.dto.response.part.PartResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.service.part.PartService;
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
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PartResponse>> create(
            @Valid @RequestBody PartRequest request) {

        PartResponse created = partService.create(request);

        ApiResponse<PartResponse> body = ApiResponse.<PartResponse>builder()
                .success(true)
                .message("Part created successfully.")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartResponse>> getById(@PathVariable UUID id) {

        PartResponse found = partService.findById(id);

        ApiResponse<PartResponse> body = ApiResponse.<PartResponse>builder()
                .success(true)
                .message("Part retrieved successfully.")
                .data(found)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PartResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<PartResponse> result = partService.findAll(page, size);

        ApiResponse<PageResponse<PartResponse>> body = ApiResponse.<PageResponse<PartResponse>>builder()
                .success(true)
                .message("Parts retrieved successfully.")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PartResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PartRequest request) {

        PartResponse updated = partService.update(id, request);

        ApiResponse<PartResponse> body = ApiResponse.<PartResponse>builder()
                .success(true)
                .message("Part updated successfully.")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<Void>> updateActive(
            @PathVariable UUID id,
            @RequestBody Map<String, Boolean> requestBody) {

        boolean active = requestBody.get("active");
        partService.updateActive(id, active);

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(true)
                .message(active ? "Part activated successfully." : "Part deactivated successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        partService.delete(id);

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(true)
                .message("Part deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(body);
    }
}