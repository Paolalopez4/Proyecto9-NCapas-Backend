package com.grupo9.auto_repair_shop.dto.request.service;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ServiceRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Base price must be a valid monetary amount")
    private BigDecimal basePrice;

    @NotNull(message = "Estimated minutes is required")
    @Positive(message = "Estimated minutes must be a positive integer")
    private Integer estimatedMinutes;
}
