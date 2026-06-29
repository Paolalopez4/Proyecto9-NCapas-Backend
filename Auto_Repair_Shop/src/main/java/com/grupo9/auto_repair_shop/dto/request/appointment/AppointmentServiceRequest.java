package com.grupo9.auto_repair_shop.dto.request.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentServiceRequest {
    @NotNull(message = "Service ID cannot be null")
    private UUID serviceId;
}
