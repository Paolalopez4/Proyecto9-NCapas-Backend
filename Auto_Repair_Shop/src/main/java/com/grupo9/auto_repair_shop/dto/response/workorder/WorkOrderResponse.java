package com.grupo9.auto_repair_shop.dto.response.workorder;

import com.grupo9.auto_repair_shop.enums.OrderType;
import com.grupo9.auto_repair_shop.enums.WorkOrderStatus;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderResponse {
    private UUID id;
    private OrderType orderType;
    private String diagnosis;
    private WorkOrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private UUID vehicleId;
    private String vehiclePlate;
    private String vehicleBrand;
    private String vehicleModel;
    private UUID mechanicId;
    private String mechanicName;
    private UUID branchId;
    private String branchName;
    private UUID appointmentId;
    private UUID budgetId;
    private UUID invoiceId;
    private UUID warrantyId;
}
