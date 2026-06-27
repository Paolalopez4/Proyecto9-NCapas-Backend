package com.grupo9.auto_repair_shop.dto.request.reminder;

import com.grupo9.auto_repair_shop.enums.ReminderType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceReminderUpdateRequest {

    @NotNull(message = "Reminder type is required")
    private ReminderType reminderType;

    private LocalDate dueDate;

    @PositiveOrZero(message = "Due mileage must be zero or positive")
    private Integer dueMileage;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}