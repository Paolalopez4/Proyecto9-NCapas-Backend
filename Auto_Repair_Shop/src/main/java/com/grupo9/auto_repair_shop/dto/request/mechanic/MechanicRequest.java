package com.grupo9.auto_repair_shop.dto.request.mechanic;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MechanicRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Branch ID is required")
    private UUID branchId;

    @NotBlank(message = "Specialty is required")
    @Size(max = 150, message = "Specialty must not exceed 150 characters")
    private String specialty;

    @NotNull(message = "Hourly rate is required")
    @Digits(integer = 8, fraction = 2, message = "Hourly rate must be a valid monetary amount")
    private BigDecimal hourlyRate;
}
