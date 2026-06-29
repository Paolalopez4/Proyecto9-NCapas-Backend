package com.grupo9.auto_repair_shop.dto.response.part;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartResponse {

    private UUID id;
    private String name;
    private String sku;
    private String category;
    private BigDecimal unitPrice;
    private boolean active;
}
