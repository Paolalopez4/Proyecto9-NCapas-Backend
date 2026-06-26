package com.grupo9.auto_repair_shop.dto.request.vehicle;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVehicleRequest {

    @NotBlank(message = "Plate is required")
    @Size(max = 50, message = "Plate must be at most 50 characters")
    private String plate;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be a valid year")
    @Max(value = 2100, message = "Year must be a valid year")
    private Integer year;

    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;

    @NotBlank(message = "VIN is required")
    @Size(max = 100, message = "VIN must not exceed 100 characters")
    private String vin;

    @PositiveOrZero(message = "Mileage must be zero or positive")
    private Integer mileage;

}
