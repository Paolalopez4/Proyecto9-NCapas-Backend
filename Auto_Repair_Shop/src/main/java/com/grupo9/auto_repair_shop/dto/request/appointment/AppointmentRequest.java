package com.grupo9.auto_repair_shop.dto.request.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {
    @NotNull(message = "Scheduled date and time is required")
    @Future(message = "Scheduled date and time must be in the future")
    private LocalDateTime scheduledAt;

    @Size(max = 500)
    private String notes;

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    @NotNull(message = "Branch ID is required")
    private UUID branchId;

    private UUID mechanicId;
}
