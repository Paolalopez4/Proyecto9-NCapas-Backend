package com.grupo9.auto_repair_shop.dto.request.inventory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;


import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchInventoryRequest {

    @NotNull(message = "Part ID is required")
    private UUID partId;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be zero or positive")
    private Integer stock;

    @NotNull(message = "Minimum stock is required")
    @PositiveOrZero(message = "Minimum stock must be zero or positive")
    private Integer stockMin;

}
