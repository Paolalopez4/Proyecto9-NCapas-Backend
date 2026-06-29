package com.grupo9.auto_repair_shop.controller.branch;

import com.grupo9.auto_repair_shop.dto.request.branch.BranchActiveRequest;
import com.grupo9.auto_repair_shop.dto.request.branch.BranchRequest;
import com.grupo9.auto_repair_shop.dto.request.branch.UpdateBranchRequest;
import com.grupo9.auto_repair_shop.dto.response.branch.BranchResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.service.branch.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchResponse>> create(
            @Valid @RequestBody BranchRequest request
    ) {
        BranchResponse branch = branchService.create(request);

        ApiResponse<BranchResponse> response = ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch created successfully")
                .data(branch)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<PageResponse<BranchResponse>>> findAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<BranchResponse> branches = branchService.findAll(active, page, size);

        ApiResponse<PageResponse<BranchResponse>> response = ApiResponse.<PageResponse<BranchResponse>>builder()
                .success(true)
                .message("Branches retrieved successfully")
                .data(branches)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<BranchResponse>> findById(
            @PathVariable UUID id
    ) {
        BranchResponse branch = branchService.findById(id);

        ApiResponse<BranchResponse> response = ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch retrieved successfully")
                .data(branch)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBranchRequest request
    ) {
        BranchResponse branch = branchService.update(id, request);

        ApiResponse<BranchResponse> response = ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch updated successfully")
                .data(branch)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchResponse>> updateActive(
            @PathVariable UUID id,
            @Valid @RequestBody BranchActiveRequest request
    ) {
        BranchResponse branch = branchService.updateActive(id, request.getActive());

        ApiResponse<BranchResponse> response = ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch status updated successfully")
                .data(branch)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        branchService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Branch deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}