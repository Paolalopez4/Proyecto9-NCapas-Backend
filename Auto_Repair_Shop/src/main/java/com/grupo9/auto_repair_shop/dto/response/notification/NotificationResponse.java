package com.grupo9.auto_repair_shop.dto.response.notification;

import com.grupo9.auto_repair_shop.enums.NotificationChannel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private UUID id;
    private String title;
    private String body;
    private NotificationChannel channel;
    private Boolean read;
    private LocalDateTime sentAt;
    private UUID userId;
    private String userName;
    private UUID workOrderId;
}
