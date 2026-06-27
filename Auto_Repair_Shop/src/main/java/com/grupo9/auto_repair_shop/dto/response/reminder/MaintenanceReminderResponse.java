package com.grupo9.auto_repair_shop.dto.response.reminder;

import com.grupo9.auto_repair_shop.enums.ReminderType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceReminderResponse {

    private UUID id;
    private ReminderType reminderType;
    private LocalDate dueDate;
    private Integer dueMileage;
    private Boolean sent;
    private Boolean acknowledged;
    private String notes;
    private LocalDateTime createdAt;

    private UUID vehicleId;
    private String vehiclePlate;
    private String vehicleBrand;

    private UUID clientId;
    private String clientName;

}
