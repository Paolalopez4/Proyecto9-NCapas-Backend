package com.grupo9.auto_repair_shop.dto.response.appointment;

import com.grupo9.auto_repair_shop.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private UUID id;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
    private String notes;
    private UUID clientId;
    private String clientName;
    private UUID vehicleId;
    private String vehiclePlate;
    private String vehicleBrand;
    private String vehicleModel;
    private UUID branchId;
    private String branchName;
    private UUID mechanicId;
    private String mechanicName;
    private UUID workOrderId;
}
