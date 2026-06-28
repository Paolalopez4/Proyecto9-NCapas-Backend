package com.grupo9.auto_repair_shop.controller.client;

import com.grupo9.auto_repair_shop.dto.request.client.ClientRequest;
import com.grupo9.auto_repair_shop.dto.request.client.UpdateClientRequest;
import com.grupo9.auto_repair_shop.dto.response.client.ClientResponse;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> create(
            @Valid @RequestBody ClientRequest request
    ) {
        ClientResponse created = clientService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ClientResponse>builder()
                        .success(true)
                        .message("Client created successfully")
                        .data(created)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<ClientResponse>> findById(
            @PathVariable UUID id
    ) {
        ClientResponse found = clientService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.<ClientResponse>builder()
                        .success(true)
                        .message("Client retrieved successfully")
                        .data(found)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ClientResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<ClientResponse> result = clientService.findAll(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ClientResponse>>builder()
                        .success(true)
                        .message("Clients retrieved successfully")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<ClientResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateClientRequest request
    ) {
        ClientResponse updated = clientService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<ClientResponse>builder()
                        .success(true)
                        .message("Client updated successfully")
                        .data(updated)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        clientService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Client deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}