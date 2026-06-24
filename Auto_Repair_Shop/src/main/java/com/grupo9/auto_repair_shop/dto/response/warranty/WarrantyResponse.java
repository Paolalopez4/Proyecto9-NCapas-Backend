package com.grupo9.auto_repair_shop.dto.response.warranty;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyResponse {

    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverage;
    private Boolean active;
    private UUID workOrderId;
    private UUID vehicleId;
    private String vehiclePlate;
    private Boolean expired;
}
