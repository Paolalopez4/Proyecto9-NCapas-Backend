package com.grupo9.auto_repair_shop.mapper.notification;

import com.grupo9.auto_repair_shop.dto.response.notification.NotificationResponse;
import com.grupo9.auto_repair_shop.entity.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .channel(notification.getChannel())
                .read(notification.getRead())
                .sentAt(notification.getSentAt())
                .userId(notification.getUser().getId())
                .userName(notification.getUser().getName())
                .workOrderId(notification.getWorkOrder() != null
                        ? notification.getWorkOrder().getId()
                        : null)
                .build();
    }
}
