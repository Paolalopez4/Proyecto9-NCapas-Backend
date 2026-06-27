package com.grupo9.auto_repair_shop.dto.request.reminder;

import com.grupo9.auto_repair_shop.enums.ReminderType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceReminderRequest {

        @NotNull(message = "Reminder type is required")
        private ReminderType reminderType;

        private LocalDate dueDate;

        @PositiveOrZero(message = "Due mileage must be zero or positive")
        private Integer dueMileage;

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;

        @NotNull(message = "Vehicle ID is required")
        private UUID vehicleId;

        @NotNull(message = "Client ID is required")
        private UUID clientId;

}
