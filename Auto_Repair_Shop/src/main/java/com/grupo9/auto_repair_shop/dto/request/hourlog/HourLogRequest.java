package com.grupo9.auto_repair_shop.dto.request.hourlog;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourLogRequest {

    @NotNull(message = "Las horas son obligatorias")
    @DecimalMin(value = "0.5", message = "El mínimo de horas por registro es 0.5")
    @DecimalMax(value = "24.0", message = "El máximo de horas por registro es 24")
    @Digits(integer = 2, fraction = 2, message = "Formato de horas inválido")
    private BigDecimal hours;

    private String notes;
}

