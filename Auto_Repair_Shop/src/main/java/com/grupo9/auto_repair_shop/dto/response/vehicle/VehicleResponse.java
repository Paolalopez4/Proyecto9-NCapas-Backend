package com.grupo9.auto_repair_shop.dto.response.vehicle;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {

    private UUID id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String vin;
    private Integer mileage;
    private LocalDateTime createdAt;

    private UUID clientId;
    private String clientName;
}
