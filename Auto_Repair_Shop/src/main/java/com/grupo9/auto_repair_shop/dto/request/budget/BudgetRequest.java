package com.grupo9.auto_repair_shop.dto.request.budget;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequest {

    @NotNull(message = "El porcentaje de impuesto es obligatorio")
    @DecimalMin(value = "0.0", message = "El impuesto no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El impuesto no puede superar el 100%")
    @Digits(integer = 3, fraction = 2, message = "Formato de impuesto inválido")
    private BigDecimal taxRate;
}
