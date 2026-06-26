package com.grupo9.auto_repair_shop.service.notification;

import com.grupo9.auto_repair_shop.dto.request.notification.NotificationRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.notification.NotificationResponse;

import java.util.UUID;

public interface NotificationService {

    NotificationResponse create(NotificationRequest request);

    void createAutomatic(UUID userId, String title, String body, UUID workOrderId);

    PageResponse<NotificationResponse> findAll(int page, int size);

    PageResponse<NotificationResponse> findMyNotifications(String email, int page, int size);

    NotificationResponse findById(UUID id);

    NotificationResponse markAsRead(UUID id, String email);

    void markAllAsRead(String email);
}
