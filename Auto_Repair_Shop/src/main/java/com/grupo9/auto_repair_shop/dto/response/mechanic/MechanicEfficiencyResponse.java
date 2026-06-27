package com.grupo9.auto_repair_shop.dto.response.mechanic;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicEfficiencyResponse {

    private UUID mechanicId;
    private String mechanicName;
    private String specialty;
    private BigDecimal hourlyRate;
    private BigDecimal totalHoursWorked;
    private long completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal laborCost;
    private BigDecimal revenuePerHour;

}