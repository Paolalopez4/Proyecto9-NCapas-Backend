package com.grupo9.auto_repair_shop.dto.request.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBranchRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String name;

    @NotBlank(message = "La dirección de la sucursal es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    private String address;

    @NotBlank(message = "El teléfono de la sucursal es obligatorio")
    @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres")
    @Pattern(
            regexp = "^[0-9+\\-\\s()]+$",
            message = "El teléfono solo puede contener números, espacios, +, -, paréntesis"
    )
    private String phone;
}