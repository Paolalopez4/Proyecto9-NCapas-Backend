package com.grupo9.auto_repair_shop.dto.request.workorder;

import com.grupo9.auto_repair_shop.enums.OrderType;
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
public class WorkOrderRequest {
    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 1000, message = "Diagnosis must be at most 1000 characters")
    private String diagnosis;

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    @NotNull(message = "Mechanic ID is required")
    private UUID mechanicId;

    @NotNull(message = "Branch ID is required")
    private UUID branchId;

    private UUID appointmentId;
}
