package com.grupo9.auto_repair_shop.controller.notification;

import com.grupo9.auto_repair_shop.dto.request.notification.NotificationRequest;
import com.grupo9.auto_repair_shop.dto.response.common.ApiResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.notification.NotificationResponse;
import com.grupo9.auto_repair_shop.service.notification.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> create(
            @Valid @RequestBody NotificationRequest request
    ) {
        NotificationResponse notification = notificationService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Notificación enviada correctamente")
                        .data(notification)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<NotificationResponse> notifications = notificationService.findAll(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<NotificationResponse>>builder()
                        .success(true)
                        .message("Notificaciones obtenidas correctamente")
                        .data(notifications)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> findMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<NotificationResponse> notifications = notificationService
                .findMyNotifications(userDetails.getUsername(), page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<NotificationResponse>>builder()
                        .success(true)
                        .message("Tus notificaciones obtenidas correctamente")
                        .data(notifications)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<NotificationResponse>> findById(
            @PathVariable UUID id
    ) {
        NotificationResponse notification = notificationService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Notificación obtenida correctamente")
                        .data(notification)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        NotificationResponse notification = notificationService
                .markAsRead(id, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Notificación marcada como leída")
                        .data(notification)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        notificationService.markAllAsRead(userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Todas las notificaciones marcadas como leídas")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
