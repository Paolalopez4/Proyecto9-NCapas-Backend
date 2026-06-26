package com.grupo9.auto_repair_shop.dto.request.workorder;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderServiceRequest {
    @NotNull(message = "Service ID cannot be null")
    private UUID serviceId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Discount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be non-negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Discount must be a valid percentage")
    private BigDecimal discount;
}
