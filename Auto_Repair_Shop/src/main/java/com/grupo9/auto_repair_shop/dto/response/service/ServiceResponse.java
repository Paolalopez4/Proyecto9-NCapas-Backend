package com.grupo9.auto_repair_shop.dto.response.service;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer estimatedMinutes;
    private Boolean active;
}
