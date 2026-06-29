package com.grupo9.auto_repair_shop.dto.request.notification;

import com.grupo9.auto_repair_shop.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID userId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar 150 caracteres")
    private String title;

    @NotBlank(message = "El cuerpo es obligatorio")
    @Size(max = 500, message = "El cuerpo no puede superar 500 caracteres")
    private String body;

    @NotNull(message = "El canal es obligatorio")
    private NotificationChannel channel;

    private UUID workOrderId;
}
