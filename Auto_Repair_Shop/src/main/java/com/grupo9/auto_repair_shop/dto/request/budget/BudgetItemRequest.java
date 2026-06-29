package com.grupo9.auto_repair_shop.dto.request.budget;

import com.grupo9.auto_repair_shop.enums.ItemType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetItemRequest {

    @NotNull(message = "El tipo de ítem es obligatorio")
    private ItemType itemType;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String description;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido")
    private BigDecimal unitPrice;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El descuento no puede superar el 100%")
    @Digits(integer = 3, fraction = 2, message = "Formato de descuento inválido")
    private BigDecimal discount;
}
