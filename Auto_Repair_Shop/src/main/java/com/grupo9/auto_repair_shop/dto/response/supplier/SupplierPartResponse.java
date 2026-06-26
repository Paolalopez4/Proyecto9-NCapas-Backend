package com.grupo9.auto_repair_shop.dto.response.supplier;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPartResponse {
    private UUID id;
    private BigDecimal price;
    private Integer leadTimeDays;
    private LocalDateTime lastUpdated;
    private UUID supplierId;
    private String supplierName;
    private UUID partId;
    private String partName;
    private String partSku;
}
